package com.paic.bst.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.Session;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Path;

/**
 * description: BertModel
 * date: 2020/11/23 3:33 下午
 * author: gallup
 * version: 1.0
 */
public class BertModel implements AutoCloseable {

    private static final int FILE_COPY_BUFFER_BYTES = 1024 * 1024;
    private static final String MODEL_DETAILS = "model.json";
    private static final String SEPARATOR_TOKEN = "[SEP]";
    private static final String START_TOKEN = "[CLS]";
    private static final String VOCAB_FILE = "vocab.txt";

    private BertModel(final SavedModelBundle bundle, final ModelDetails model, final Path vocabulary) {
        tokenizer = new FullTokenizer(vocabulary, model.doLowerCase);
        this.bundle = bundle;
        this.model = model;

        final int[] ids = tokenizer.convert(new String[]{START_TOKEN, SEPARATOR_TOKEN});
        startTokenId = ids[0];
        separatorTokenId = ids[1];
    }

//    private BertModel(final SavedModelBundle bundle, final Path vocabulary) {
//        tokenizer = new FullTokenizer(vocabulary, model.doLowerCase);
//        this.bundle = bundle;
//
//        final int[] ids = tokenizer.convert(new String[]{START_TOKEN, SEPARATOR_TOKEN});
//        startTokenId = ids[0];
//        separatorTokenId = ids[1];
//    }

    @Override
    public void close() {
        bundle.close();
    }

    private static class ModelDetails {
        public boolean doLowerCase;
        public String inputIds, inputMask, segmentIds, pooledOutput, sequenceOutput;
        public int maxSequenceLength;
    }

    private final SavedModelBundle bundle;
    private final ModelDetails model;
    private final int separatorTokenId;
    private final int startTokenId;
    private final FullTokenizer tokenizer;


    private class Inputs implements AutoCloseable {
        private final Tensor<Integer> inputIds, inputMask, segmentIds;

        public Inputs(final IntBuffer inputIds, final IntBuffer inputMask, final IntBuffer segmentIds, final int count) {
            this.inputIds = Tensor.create(new long[]{count, model.maxSequenceLength}, inputIds);
            this.inputMask = Tensor.create(new long[]{count, model.maxSequenceLength}, inputMask);
            this.segmentIds = Tensor.create(new long[]{count, model.maxSequenceLength}, segmentIds);
        }

        @Override
        public void close() {
            inputIds.close();
            inputMask.close();
            segmentIds.close();
        }
    }


    private Inputs getInputs(final String sequence) {
        final String[] tokens = tokenizer.tokenize(sequence);

        final IntBuffer inputIds = IntBuffer.allocate(model.maxSequenceLength);
        final IntBuffer inputMask = IntBuffer.allocate(model.maxSequenceLength);
        final IntBuffer segmentIds = IntBuffer.allocate(model.maxSequenceLength);

        /*
         * In BERT:
         * inputIds are the indexes in the vocabulary for each token in the sequence
         * inputMask is a binary mask that shows which inputIds have valid data in them
         * segmentIds are meant to distinguish paired sequences during training tasks. Here they're always 0 since we're only doing inference.
         */
        final int[] ids = tokenizer.convert(tokens);
        inputIds.put(startTokenId);
        inputMask.put(1);
        segmentIds.put(0);
        for (int i = 0; i < ids.length && i < model.maxSequenceLength - 2; i++) {
            inputIds.put(ids[i]);
            inputMask.put(1);
            segmentIds.put(0);
        }
        inputIds.put(separatorTokenId);
        inputMask.put(1);
        segmentIds.put(0);

        while (inputIds.position() < model.maxSequenceLength) {
            inputIds.put(0);
            inputMask.put(0);
            segmentIds.put(0);
        }

        inputIds.rewind();
        inputMask.rewind();
        segmentIds.rewind();

        return new Inputs(inputIds, inputMask, segmentIds, 1);
    }

    private Inputs getInputs(final String[] sequences) {
        final String[][] tokens = tokenizer.tokenize(sequences);

        final IntBuffer inputIds = IntBuffer.allocate(sequences.length * model.maxSequenceLength);
        final IntBuffer inputMask = IntBuffer.allocate(sequences.length * model.maxSequenceLength);
        final IntBuffer segmentIds = IntBuffer.allocate(sequences.length * model.maxSequenceLength);

        /*
         * In BERT:
         * inputIds are the indexes in the vocabulary for each token in the sequence
         * inputMask is a binary mask that shows which inputIds have valid data in them
         * segmentIds are meant to distinguish paired sequences during training tasks. Here they're always 0 since we're only doing inference.
         */
        int instance = 1;
        for (final String[] token : tokens) {
            final int[] ids = tokenizer.convert(token);
            inputIds.put(startTokenId);
            inputMask.put(1);
            segmentIds.put(0);
            for (int i = 0; i < ids.length && i < model.maxSequenceLength - 2; i++) {
                inputIds.put(ids[i]);
                inputMask.put(1);
                segmentIds.put(0);
            }
            inputIds.put(separatorTokenId);
            inputMask.put(1);
            segmentIds.put(0);

            while (inputIds.position() < model.maxSequenceLength * instance) {
                inputIds.put(0);
                inputMask.put(0);
                segmentIds.put(0);
            }
            instance++;
        }

        inputIds.rewind();
        inputMask.rewind();
        segmentIds.rewind();

        return new Inputs(inputIds, inputMask, segmentIds, sequences.length);
    }


    public static BertModel load(Path path) {
        path = path.toAbsolutePath();
        ModelDetails model;
        try {
            model = new ObjectMapper().readValue(path.resolve("assets").resolve(MODEL_DETAILS).toFile(), ModelDetails.class);
        } catch(final IOException e) {
            throw new RuntimeException(e);
        }

        return new BertModel(SavedModelBundle.load(path.toString(), "serve"), model, path.resolve("assets").resolve(VOCAB_FILE));
    }

//    public Tensor predict(Tensor tensorInput) {
//        // feed()传参类似python端的feed_dict
//        // fetch()指定输出节点的名称
//        Tensor output = this.tensorflowSession.runner().feed("input_x", tensorInput).fetch("out_y").run().get(0);
//
//        return output;
//    }
//
//    public static void main(String[] args) {
//        // 创建输入tensor, 注意type、shape应和训练时一致
//        int[][] testvec = new int[1][100];
//        for (int i = 0; i < 100; i++) {
//            testvec[0][i] = i;
//        }
//        Tensor input = Tensor.create(testvec);
//
//        // load 模型
//        String path = BertModel.class.getResource("/distilbert_squad/").getPath();
//        System.out.println("Loading model at " + path);
//        BertModel model = new BertModel(bundle);
//        model.load(path);
//
//        // 模型推理，注意resultValues的type、shape
//        Tensor out = model.predict(input);
//        float[][] resultValues = (float[][]) out.copyTo(new float[1][10]);
//        // 防止内存泄漏，释放tensor内存
//        input.close();
//        out.close();
//        // 结果输出
//        for (int i = 0; i < 10; i++) {
//            System.out.println(resultValues[0][i]);
//        }
//    }
}

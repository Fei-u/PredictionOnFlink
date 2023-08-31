package flink_operator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;

import entity.JsonEntity;
import entity.XgboostEntity;
import utils.Const;

public class XgboostPredFunction implements MapFunction<XgboostEntity, JsonEntity>{

    @Override
    public JsonEntity map(XgboostEntity entity) throws Exception {
        // 加载模型
        ModelEvaluator<?> modelEvaluator = loadModel();

        // 准备输入特征
        Map<FieldName, Double> inputFeatures = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            int index = 5 * i + 1;
            inputFeatures.put(FieldName.create("x" + (index + 1)), entity.getValues()[i]);
            inputFeatures.put(FieldName.create("x" + (index + 2)), entity.getMeans()[i]);
            inputFeatures.put(FieldName.create("x" + (index + 3)), entity.getVariances()[i]);
            inputFeatures.put(FieldName.create("x" + (index + 4)), entity.getUppers()[i]);
            inputFeatures.put(FieldName.create("x" + (index + 5)), entity.getLowers()[i]);
        }

        // 获取推理输出
        Map<FieldName, ?> results = modelEvaluator.evaluate(inputFeatures);
        String predictionObj = results.get(FieldName.create("y")).toString();    
        Double prediction = null;

        // 截取结果
        int startIndex = predictionObj.indexOf('=') + 1;
        int endIndex = predictionObj.indexOf('}');
        if (startIndex >= 0 && endIndex > startIndex) {
            String valueString = predictionObj.substring(startIndex, endIndex);
            try {
                prediction = Double.parseDouble(valueString);
            } catch (NumberFormatException e) {
                System.out.println("Failed to parse value: " + valueString);
            }
        } else {
            System.out.println("Invalid input format");
        }
    

        // 封装为JSONEntity输出
        return new JsonEntity(entity.getPi_tag(), entity.getPi_time(), null, prediction, null);
    }

    private ModelEvaluator<?> loadModel() throws Exception {
        try {
            File modelFile = new File(Const.modelPath);
            InputStream is = new FileInputStream(modelFile);
            PMML pmml = PMMLUtil.unmarshal(is);
    
            ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
            ModelEvaluator<?> modelEvaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
            return modelEvaluator;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}

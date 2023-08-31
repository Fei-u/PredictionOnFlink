package xgboost;
// package XGBoost;

// import org.jpmml.evaluator.*;
// import org.xml.sax.SAXException;

// import javax.xml.bind.JAXBException;
// import java.io.File;
// import java.io.IOException;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;

// public class pmml_xgboost {

//     public static void main(String[] args) throws JAXBException, SAXException, IOException {
//         // Building a model evaluator from a PMML file
//         Evaluator evaluator = new LoadingModelEvaluatorBuilder()
//         .load(new File("model.pmml"))
//         .build();

//         // Perforing the self-check
//         evaluator.verify();

//         // Printing input (x1, x2, .., xn) fields
//         List<InputField> inputFields = evaluator.getInputFields();
//         System.out.println("Input fields: " + inputFields);

//         // Printing primary result (y) field(s)
//         List<TargetField> targetFields = evaluator.getTargetFields();
//         System.out.println("Target field(s): " + targetFields);

//         // Printing secondary result (eg. probability(y), decision(y)) fields
//         List<OutputField> outputFields = evaluator.getOutputFields();
//         System.out.println("Output fields: " + outputFields);

//         // Iterating through columnar data (eg. a CSV file, an SQL result set)
//         while(true){
//             // Reading a record from the data source
//             Map<String, ?> arguments = readRecord();
//             if(arguments == null){
//             break;
//             }

//             // Evaluating the model
//             Map<String, ?> results = evaluator.evaluate(arguments);

//             // Decoupling results from the JPMML-Evaluator runtime environment
//             results = EvaluatorUtil.decodeAll(results);

//             // Writing a record to the data sink
//             writeRecord(results);
//         }

//         // Making the model evaluator eligible for garbage collection
//         evaluator = null;
//     }
// }

# PredictionOnFlink
  
This is a guide outlining the process of conducting a toy time-series prediction using Flink.

## Step 1: Data Generation and Ingestion

To begin the time-series prediction process in Flink, the first step involves sending simulated sensor data to RabbitMQ in JSON format. The JSON structure resembles: { "machineId": 1, "time": 1, "sensorId": 1, "sensorVal": 1, "sensorState": "good", "sensorID": 2, ... }.

## Step 2: Stream Processing

Upon receiving the sensor data stream from RabbitMQ, the next step is to read and process the data. This is achieved by converting the JSON data into POJO format. Additionally, some simple feature transformations such as addition, mean calculation, variance computation, and quartile calculations (upper and lower quartiles) are performed on the data.

involved operators: addSource/flatMap/watermark/filter/window/keyBy/process

## Step 3: Data Transformation for LGBM

The data is further transformed into the required format for LightGBM (LGBM). The transformed data is then processed using a map operator, and LGBM's PMML (Predictive Model Markup Language) file is employed to make predictions.

involved operators: keyBy/process/map

## Step 4: Comparison, Merging, and Storage

In the final stage, the predicted results are compared with the original data stream, and the two streams are merged. The merged data is written to both Redis and InfluxDB databases for storage and analysis.

involved operators: CoGroup/where/equalTo/Window/apply/addSink
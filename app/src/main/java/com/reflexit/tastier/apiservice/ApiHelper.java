package com.reflexit.tastier.apiservice;

public class ApiHelper {

    public static String BASE_URL = "https://francecentral.api.cognitive.microsoft.com/face/v1.0/";
    public static final String OUTH_KEY = "******************";
    public final static String DETECT = BASE_URL + "detect?returnFaceId=true&returnFaceLandmarks=false&recognitionModel=recognition_02&returnRecognitionModel=false&detectionModel=detection_01";
    public final static String IDENTIFY = BASE_URL + "identify";
    public final static String ADD_FACE = BASE_URL + "persongroups/tastebuddies/persons/personId/persistedFaces?userData&targetFace&detectionModel=detection_01";
    public final static String ADD_PERSON = BASE_URL + "persongroups/tastebuddies/persons";
    public final static String TRAIN = BASE_URL + "persongroups/tastebuddies/train";
    public final static String GET_ALL_PERSONS = BASE_URL + "persongroups/tastebuddies/persons";
    public final static String DELETE_GROUP = BASE_URL +  "persongroups/tastebuddies";

}

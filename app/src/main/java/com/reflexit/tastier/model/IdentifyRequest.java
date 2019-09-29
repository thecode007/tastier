package com.reflexit.tastier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifyRequest {

        private String personGroupId;
        private List<String> faceIds = new ArrayList<>();
        private Integer maxNumOfCandidatesReturned;
        private Double confidenceThreshold;


        public String getPersonGroupId() {
            return personGroupId;
        }

        public void setPersonGroupId(String personGroupId) {
            this.personGroupId = personGroupId;
        }

        public List<String> getFaceIds() {
            return faceIds;
        }

        public void setFaceIds(List<String> faceIds) {
            this.faceIds = faceIds;
        }

        public void addFaceId(String faceID) {
            faceIds.add(faceID);
        }

        public Integer getMaxNumOfCandidatesReturned() {
            return maxNumOfCandidatesReturned;
        }

        public void setMaxNumOfCandidatesReturned(Integer maxNumOfCandidatesReturned) {
            this.maxNumOfCandidatesReturned = maxNumOfCandidatesReturned;
        }

        public Double getConfidenceThreshold() {
            return confidenceThreshold;
        }

        public void setConfidenceThreshold(Double confidenceThreshold) {
            this.confidenceThreshold = confidenceThreshold;
        }

    }

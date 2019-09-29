package com.reflexit.tastier.model;


import java.util.List;

public class FaceCandidate {

    private String faceId;
    private List<Candidate> candidates = null;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

}

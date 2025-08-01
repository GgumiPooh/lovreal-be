    package com.lovreal_be.DTO;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class CoupleResponse {
        private String id;
        private String sendCouple;
        private String coupleRequest;

        public CoupleResponse(String id, String coupleId, String coupleRequest) {
            this.id = id;
            this.sendCouple = coupleId;
            this.coupleRequest = coupleRequest;
        }
    }
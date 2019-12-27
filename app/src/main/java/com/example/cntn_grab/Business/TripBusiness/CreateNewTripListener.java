package com.example.cntn_grab.Business.TripBusiness;

public interface CreateNewTripListener {
    void createNewTripDidStart();
    void createNewTripDidEnd(Boolean isOk, String tripID);
}

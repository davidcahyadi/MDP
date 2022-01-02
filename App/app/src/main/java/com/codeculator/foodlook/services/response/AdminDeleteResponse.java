package com.codeculator.foodlook.services.response;

import android.os.Parcel;
import android.os.Parcelable;

public class AdminDeleteResponse implements Parcelable {
    private String message;

    public String getMessage() {
        return message;
    }

    public AdminDeleteResponse (String message){
        this.message = message;
    }

    protected AdminDeleteResponse(Parcel in) {
        message = in.readString();
    }

    public static final Creator<AdminDeleteResponse> CREATOR = new Creator<AdminDeleteResponse>() {
        @Override
        public AdminDeleteResponse createFromParcel(Parcel in) {
            return new AdminDeleteResponse(in);
        }

        @Override
        public AdminDeleteResponse[] newArray(int size) {
            return new AdminDeleteResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
    }
}

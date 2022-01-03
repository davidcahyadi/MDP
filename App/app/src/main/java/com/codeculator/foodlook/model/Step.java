package com.codeculator.foodlook.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps")
public class Step implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "type_id")
    public int typeId;

    public Step(int id, int order, String title, String url, String description, int duration){
        this.id = id;
        this.order = order;
        this.title = title;
        if(url.equals("0")) this.url = "";
        else this.url = url;
        this.description = description;
        this.duration = duration;
    }

    @Ignore
    public Step(int id, int order ,int recipeId, String title, String url, String description, int duration){
        this.id = id;
        this.recipeId = recipeId;
        this.order = order;
        this.title = title;
        if(url.equals("0")) this.url = "";
        else this.url = url;
        this.description = description;
        this.duration = duration;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        recipeId = in.readInt();
        order = in.readInt();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        duration = in.readInt();
        typeId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(recipeId);
        dest.writeInt(order);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeInt(duration);
        dest.writeInt(typeId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}


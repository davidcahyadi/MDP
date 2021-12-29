package com.codeculator.foodlook.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.util.Date;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "user_id")
    public int user_id;
    @ColumnInfo(name = "rate")
    public float rate;
    @ColumnInfo(name = "view")
    public int view;
    @ColumnInfo(name = "like")
    public int like;
    @ColumnInfo(name = "cook_duration")
    public int cook_duration;
    @ColumnInfo(name = "prep_duration")
    public int prep_duration;
    @ColumnInfo(name = "serve_portion")
    public int serve_portion;
    @ColumnInfo(name = "crawling_from")
    public String crawling_from;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "created_at")
    public String created_at;
    @ColumnInfo(name = "updated_at")
    public String updated_at;
    @ColumnInfo(name = "photo")
    public String photo;

    public Recipe(int id, String title, int user_id, float rate, int view, int like, int cook_duration, int prep_duration, int serve_portion, String description, String created_at, String updated_at, String photo,String crawling_from) {
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.rate = rate;
        this.view = view;
        this.like = like;
        this.cook_duration = cook_duration;
        this.prep_duration = prep_duration;
        this.serve_portion = serve_portion;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.photo = photo;
        this.crawling_from = crawling_from;
    }

    public Recipe(JSONObject obj){
        try{
            this.id = obj.getInt("id");
            this.title = obj.getString("title");
            this.user_id = obj.getInt("user_id");
            this.rate = (float) obj.getDouble("rate");
            this.view = obj.getInt("view");
            this.like = obj.getInt("like");
            this.cook_duration = obj.getInt("cook_duration");
            this.prep_duration = obj.getInt("prep_duration");
            this.serve_portion = obj.getInt("serve_portion");
            this.description = obj.getString("description");
            this.created_at = obj.getString("created_at");
            this.updated_at = obj.getString("updated_at");
            this.photo = obj.getString("photo");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        user_id = in.readInt();
        rate = in.readFloat();
        view = in.readInt();
        like = in.readInt();
        cook_duration = in.readInt();
        prep_duration = in.readInt();
        serve_portion = in.readInt();
        description = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(user_id);
        dest.writeFloat(rate);
        dest.writeInt(view);
        dest.writeInt(like);
        dest.writeInt(cook_duration);
        dest.writeInt(prep_duration);
        dest.writeInt(serve_portion);
        dest.writeString(description);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
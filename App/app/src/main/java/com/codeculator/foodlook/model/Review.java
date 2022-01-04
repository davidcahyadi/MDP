package com.codeculator.foodlook.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "review")
public class Review implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "user_id")
    public int user_id;
    @ColumnInfo(name = "recipe_id")
    public int recipe_id;
    @ColumnInfo(name = "rate")
    public int rate ;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "review_id")
    public int review_id ;
    @ColumnInfo(name = "created_at")
    public String created_at;
    @ColumnInfo(name = "deleted_at")
    public String deleted_at;

    @ColumnInfo(name= "name")
    public String name;

    public Review(int id, int user_id, int recipe_id, int rate, String description, int review_id, String created_at, String deleted_at, String name) {
        this.id = id;
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.rate = rate;
        this.description = description;
        this.review_id = review_id;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.name = name;
    }

    protected Review(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        recipe_id = in.readInt();
        rate = in.readInt();
        description = in.readString();
        review_id = in.readInt();
        created_at = in.readString();
        deleted_at = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(user_id);
        dest.writeInt(recipe_id);
        dest.writeInt(rate);
        dest.writeString(description);
        dest.writeInt(review_id);
        dest.writeString(created_at);
        dest.writeString(deleted_at);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}

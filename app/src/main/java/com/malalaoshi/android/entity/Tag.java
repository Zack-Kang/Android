package com.malalaoshi.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zl on 15/12/14.
 */
public class Tag extends BaseEntity{

    public Tag(){

    }

    public Tag(Long id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static List<Tag> tags;

    static {
        tags = new ArrayList<Tag>();

//        tags.add(new Tag(1L, "不错"));
//        tags.add(new Tag(2L, "幽默"));
//        tags.add(new Tag(3L, "有责任心"));
//        tags.add(new Tag(4L, "正能量"));
    }

    public static String generateTagViewString(Long[] tagsId, List<Tag> tagList) {
        if (tagsId == null || tagList == null || tagsId.length == 0 || tagList.size() == 0) {
            return null;
        }
        String str = "";
        for (int i = 0; i < tagsId.length; i++) {
            for (Tag tag : tagList) {
                if (tagsId[i].equals(tag.getId())) {
                    str += tag.getName();
                    if (i < tagsId.length - 1) {
                        str += " | ";
                    }
                }
            }
        }

        return str;
    }

    @Deprecated
    public static Tag findTagById(Long tagId, List<Tag> allTag) {
        for (Tag tag : allTag) {
            if (tag.getId().equals(tagId)) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Tag(Parcel in) {
        super(in);
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}

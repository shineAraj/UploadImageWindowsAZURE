package com.example.tec.uploadimagetoazure;
/**
 * Created by SHINE on 9/22/2015.
 */

import com.google.gson.annotations.SerializedName;

/**


 This is a pojo class for table User in your azure mobile service table/

 I suggest you to make same name table as this pojo class and same column names as gson
 serialsised name . Eg : name , email  (@com.google.gson.annotations.SerializedName("name"))


 */




public class User {



    /**
     * Item Id
     *
     * An id is required and it is automaticaly generated by mobile servuce table
     */
    @SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @SerializedName("complete")
    private boolean mComplete;



    /**
     * Item text
     */
    @SerializedName("stringimage")
    private String mstringimage;


    /**
     * User constructor
     */
    public User() {

    }








    /**
     * Initializes a new User
     *
     * @param name
     *            The item text
     * @param id
     *            The item id
     */
    public User(String name, String id, String stringimage) {
       this.setimagestring(stringimage);


    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }







    //SET METHODS




    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }



    public final void setimagestring(String stringimage) {
        mstringimage = stringimage;
    }





    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && ((User) o).mId == mstringimage;
    }
}



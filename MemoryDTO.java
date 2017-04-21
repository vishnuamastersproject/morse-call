package com.project.vactionbook;

import java.util.Arrays;


public class MemoryDTO
{

    // private variables
    int _id;
    String _name;
   public String _memoryTitle, _memoryDescription, _memoryDate;
    byte[] _image;

    // Empty constructor
    public MemoryDTO() {

    }

    public String get_memoryDescription() {
        return _memoryDescription;
    }

    public void set_memoryDescription(String _memoryDescription) {
        this._memoryDescription = _memoryDescription;
    }

    public String get_memoryTitle() {
        return _memoryTitle;
    }

    public void set_memoryTitle(String _memoryTitle) {
        this._memoryTitle = _memoryTitle;
    }

    public String get_memoryDate() {
        return _memoryDate;
    }

    public void set_memoryDate(String _memoryDate) {
        this._memoryDate = _memoryDate;
    }

    public MemoryDTO(String _memoryTitle, String _memoryDescription, String _memoryDate) {

        this._memoryTitle = _memoryTitle;
        this._memoryDescription = _memoryDescription;
        this._memoryDate = _memoryDate;
    }

    // constructor
    @Override
    public String toString() {
        return "MemoryDTO{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                ", _memoryTitle='" + _memoryTitle + '\'' +
                ", _memoryDescription='" + _memoryDescription + '\'' +
                ", _memoryDate='" + _memoryDate + '\'' +
                ", _image=" + Arrays.toString(_image) +
                '}';
    }public MemoryDTO(int keyId, String name, byte[] image) {
        this._id = keyId;
        this._name = name;
        this._image = image;

    }
    public MemoryDTO(String name, byte[] image) {
        this._name = name;
        this._image = image;

    }
    public MemoryDTO(int keyId) {
        this._id = keyId;

    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int keyId) {
        this._id = keyId;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public byte[] getImage() {
        return this._image;
    }

    // setting phone number
    public void setImage(byte[] image) {
        this._image = image;
    }


}
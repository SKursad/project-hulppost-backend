package nl.novi.hulppost.model;

import nl.novi.hulppost.util.ValidImage;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    @OneToOne
    private Request request;

    public Attachment(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }


    public Attachment(String fileName, String fileType, byte[] data, Request request) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.request = request;
    }

    public Attachment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}

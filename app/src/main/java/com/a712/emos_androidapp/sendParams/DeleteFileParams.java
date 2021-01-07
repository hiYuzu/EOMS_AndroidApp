package com.a712.emos_androidapp.sendParams;

/**
 * Created by Lenovo on 2017/7/5.
 */

public class DeleteFileParams {
    private String fileId;// 文件ID
    private String tfType;//故障或任务类型

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTfType() {
        return tfType;
    }

    public void setTfType(String tfType) {
        this.tfType = tfType;
    }

}

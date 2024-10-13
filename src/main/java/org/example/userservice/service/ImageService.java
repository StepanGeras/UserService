package org.example.userservice.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.example.userservice.exception.book.BookFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public ImageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public GridFSFile fileFindGridFs(String imageId) {

        GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(imageId)));
        if (gridFsFile == null) {
            throw new BookFileNotFoundException("File not found in GridFS");
        }

        return gridFsFile;

    }

}

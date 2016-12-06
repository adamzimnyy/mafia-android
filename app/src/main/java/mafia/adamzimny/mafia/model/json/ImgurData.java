package mafia.adamzimny.mafia.model.json;

import java.util.HashMap;
import java.util.Map;

public class ImgurData {

    private Data data;
    private Boolean success;
    private Integer status;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.getAdditionalProperties().put(name, value);
    }

    public Data getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

 public class Data {

        private String id;
        private Object title;
        private Object description;
        private Integer datetime;
        private String type;
        private Boolean animated;
        private Integer width;
        private Integer height;
        private Integer size;
        private Integer views;
        private Integer bandwidth;
        private Object vote;
        private Boolean favorite;
        private Object nsfw;
        private Object section;
        private Object accountUrl;
        private Integer accountId;
        private Boolean inGallery;
        private String deletehash;
        private String name;
        private String link;
        private Map<String, Object> additionalProperties = new HashMap<>();

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.getAdditionalProperties().put(name, value);
        }

        public String getId() {
            return id;
        }

        public Object getTitle() {
            return title;
        }

        public Object getDescription() {
            return description;
        }

        public Integer getDatetime() {
            return datetime;
        }

        public String getType() {
            return type;
        }

        public Boolean getAnimated() {
            return animated;
        }

        public Integer getWidth() {
            return width;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getSize() {
            return size;
        }

        public Integer getViews() {
            return views;
        }

        public Integer getBandwidth() {
            return bandwidth;
        }

        public Object getVote() {
            return vote;
        }

        public Boolean getFavorite() {
            return favorite;
        }

        public Object getNsfw() {
            return nsfw;
        }

        public Object getSection() {
            return section;
        }

        public Object getAccountUrl() {
            return accountUrl;
        }

        public Integer getAccountId() {
            return accountId;
        }

        public Boolean getInGallery() {
            return inGallery;
        }

        public String getDeletehash() {
            return deletehash;
        }

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public void setDatetime(Integer datetime) {
            this.datetime = datetime;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setAnimated(Boolean animated) {
            this.animated = animated;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public void setViews(Integer views) {
            this.views = views;
        }

        public void setBandwidth(Integer bandwidth) {
            this.bandwidth = bandwidth;
        }

        public void setVote(Object vote) {
            this.vote = vote;
        }

        public void setFavorite(Boolean favorite) {
            this.favorite = favorite;
        }

        public void setNsfw(Object nsfw) {
            this.nsfw = nsfw;
        }

        public void setSection(Object section) {
            this.section = section;
        }

        public void setAccountUrl(Object accountUrl) {
            this.accountUrl = accountUrl;
        }

        public void setAccountId(Integer accountId) {
            this.accountId = accountId;
        }

        public void setInGallery(Boolean inGallery) {
            this.inGallery = inGallery;
        }

        public void setDeletehash(String deletehash) {
            this.deletehash = deletehash;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setAdditionalProperties(Map<String, Object> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
}
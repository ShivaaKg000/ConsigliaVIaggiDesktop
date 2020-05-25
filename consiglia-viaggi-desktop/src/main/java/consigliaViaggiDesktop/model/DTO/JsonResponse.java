package consigliaViaggiDesktop.model.DTO;

import java.io.Serializable;

public class JsonResponse implements Serializable {

    private String message;
    private Boolean response;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public JsonResponse(Boolean response, String message) {
        this.response = response;
        this.message = message;
    }
    public String getMessage() {

        return this.message;
    }
    public Boolean getResponse() {

        return this.response;
    }
}

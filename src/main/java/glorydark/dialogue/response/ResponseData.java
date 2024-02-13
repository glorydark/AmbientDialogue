package glorydark.dialogue.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

/**
 * @author glorydark
 */
@NoArgsConstructor
@Data
public class ResponseData {

    public static final ResponseData EMPTY_RESPONSE_DATA = new ResponseData();

    private LinkedHashMap<ResponseDataType, Object> responses = new LinkedHashMap<>();

    public void setResponse(ResponseDataType responseDataType, Object object) {
        responses.put(responseDataType, object);
    }

    public Object getResponse(ResponseDataType responseDataType) {
        return responses.get(responseDataType);
    }

    public boolean getBooleanResponse(ResponseDataType responseDataType) {
        return (boolean) responses.getOrDefault(responseDataType, true);
    }
}

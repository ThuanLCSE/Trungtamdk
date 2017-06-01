package home.smart.thuans.centraldevice.http.pojoDto;

/**
 * Created by Sam on 4/14/2017.
 */
public class FunctionIntentDTO {
    private int id;
    private String functionName;
    private String targetType;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}

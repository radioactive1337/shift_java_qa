import java.util.Map;

public class Qw {
    public static void main(String[] args) {
        Map<String, String> queryParams = Map.of(
                "repetitionCount", "1",
                "soundCount", "1",
                "id", "1"
        );
        System.out.println("http://localhost:2222/api/duck/action/quack" + getQueryParams(queryParams));
    }

    private static String getQueryParams(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return "";
        }

        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // remove trailing &
        return "?" + queryBuilder;
    }
}

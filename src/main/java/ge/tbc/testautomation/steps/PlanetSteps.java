package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.api.client.PlanetApi;
import ge.tbc.testautomation.data.model.response.PlanetDetailResponse;
import ge.tbc.testautomation.data.model.response.PlanetListResponse;
import io.restassured.response.Response;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlanetSteps {

    private final PlanetApi planetApi = new PlanetApi();

    public PlanetListResponse getPlanets() {
        Response resp = planetApi.getPlanets();
        return resp.as(PlanetListResponse.class);
    }

    public List<PlanetDetailResponse> getPlanetDetails(PlanetListResponse listResponse) {
        return listResponse.getResults().stream()
                .map(r -> planetApi.getPlanetDetail(r.getUrl()).as(PlanetDetailResponse.class))
                .collect(Collectors.toList());
    }

    public List<PlanetDetailResponse> getTop3ByEdited(List<PlanetDetailResponse> planets) {
        return planets.stream()
                .sorted(Comparator.comparing(
                        p -> p.getResult().getProperties().getEdited(), Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
    }

    public PlanetDetailResponse getMaxRotation(List<PlanetDetailResponse> planets) {
        return planets.stream()
                .max(Comparator.comparingInt(
                        p -> Integer.parseInt(p.getResult().getProperties().getRotation_period())))
                .orElseThrow();
    }
}

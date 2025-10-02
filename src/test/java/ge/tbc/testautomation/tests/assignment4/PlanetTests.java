package ge.tbc.testautomation.tests.assignment4;

import ge.tbc.testautomation.data.model.response.PlanetDetailResponse;
import ge.tbc.testautomation.data.model.response.PlanetListResponse;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import ge.tbc.testautomation.steps.PlanetSteps;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class PlanetTests {

    private final PlanetSteps planetSteps = new PlanetSteps();

    @Test
    public void testPlanets() {
        // 1. Get list of planets
        PlanetListResponse listResponse = planetSteps.getPlanets();

        // 2. Fetch details for each planet
        List<PlanetDetailResponse> details = planetSteps.getPlanetDetails(listResponse);

        // 3. Top 3 most recently edited planets
        List<PlanetDetailResponse> top3Recent = planetSteps.getTop3ByEdited(details);
        top3Recent.forEach(p ->
                System.out.println(p.getResult().getProperties().getName() + " | Edited: " +
                        p.getResult().getProperties().getEdited()));

        // 4. Planet with max rotation period
        PlanetDetailResponse maxRotation = planetSteps.getMaxRotation(details);
        System.out.println("Planet with max rotation period: " +
                maxRotation.getResult().getProperties().getName());

        // 5. Example validations
        PlanetDetailResponse first = details.get(0);
        assertThat(first.getMessage(), Matchers.equalTo("ok"));
        assertThat(first.getResult().getProperties().getName(), Matchers.notNullValue());
        assertThat(first.getResult().getProperties().getRotation_period(), Matchers.notNullValue());
        assertThat(first.getResult().getProperties().getDiameter(), Matchers.notNullValue());
        assertThat(first.getResult().getProperties().getClimate(), Matchers.not(Matchers.isEmptyString()));
    }
}

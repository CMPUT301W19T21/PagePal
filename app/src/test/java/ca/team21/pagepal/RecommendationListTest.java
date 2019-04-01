package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import ca.team21.pagepal.models.HistoryItem;
import ca.team21.pagepal.models.RecommendationList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests the recommendation list
 */
public class RecommendationListTest {

    /**
     * Tests generating the recommendation list
     */
    @Test
    public void generateListTest() {
        HistoryItem history = new HistoryItem();
        Location location = new Location(LocationManager.NETWORK_PROVIDER);

        RecommendationList recommendations = new RecommendationList(history, location);
        recommendations.generateList();
        assertNotEquals(null, recommendations.get());
    }
}

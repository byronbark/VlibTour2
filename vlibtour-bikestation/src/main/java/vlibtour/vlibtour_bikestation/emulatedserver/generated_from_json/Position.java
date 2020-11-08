
package vlibtour.vlibtour_bikestation.emulatedserver.generated_from_json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import vlibtour.vlibtour_bikestation.emulatedserver.Haversine;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lat",
    "lng"
})
public class Position {

    @JsonProperty("lat")
    public double lat;
    @JsonProperty("lng")
    public double lng;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Position() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public Position(double lat, double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lat", lat).append("lng", lng).toString();
    }

    public Boolean nearTo(final double latitude, final double longitude, final double distance){
        // System.out.println(this.lat+" "+this.lng+" "+latitude+" "+longitude+" "+distance/1000.0+" "+(Haversine.haversine(this.lat, this.lng, latitude, longitude) <= (distance/1000.0)));
        return (Haversine.haversine(this.lat, this.lng, latitude, longitude) <= (distance/1000.0));

    }

    public double distanceFrom(final double latitude, final double longitude){
        return (Haversine.haversine(this.lat, this.lng, latitude, longitude));
    }

}

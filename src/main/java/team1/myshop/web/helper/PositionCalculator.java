package team1.myshop.web.helper;

public class PositionCalculator {

	/**
	 * Calculates the longitude offset of a point which is distance kilometers away
	 * 
	 * @param latitude Latitude of the initial point
	 * @param distance displacement of the point whose longitude offset is calculated
	 * @return longitude degree offset of a point distance kilometers away
	 * @throws IllegalArgumentException invalid latitude data: latitude outside [-90,90]
	 */
	public static double calcLongitudeOffset(double latitude, double distance) throws IllegalArgumentException {
		if (Math.abs(latitude) > 90) {
			throw new IllegalArgumentException("invalid latitude");
		}

		final double earthRadius = 6371; // [km]

		return Math.toDegrees(distance / (earthRadius * Math.cos(Math.toRadians(latitude))));
	}

	/**
	 * Calculates the latitude offset of a point which is distance kilometers away
	 * 
	 * @param distance displacement of the point whose latitude offset is calculated
	 * @return latitude degree offset of a point distance kilometers away
	 */
	public static double calcLatitudeOffset(double distance) {
		final double earthRadius = 6371; // [km]

		return Math.toDegrees(distance / earthRadius);
	}
	
}

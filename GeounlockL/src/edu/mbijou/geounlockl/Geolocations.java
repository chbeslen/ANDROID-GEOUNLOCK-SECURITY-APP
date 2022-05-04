package edu.mbijou.geounlockl;

public class Geolocations
{
	public class geolocs{
		private long loctime;
		private double lat;
		private double lng;
		public geolocs(long t,double lt,double lg){
			setLoctime(t);
			setLat(lt);
			setLng(lg);
		}
		public long getLoctime() {
			return loctime;
		}
		public void setLoctime(long loctime) {
			this.loctime = loctime;
		}
		public double getLat() {
			return lat;
		}
		public void setLat(double lat) {
			this.lat = lat;
		}
		public double getLng() {
			return lng;
		}
		public void setLng(double lng) {
			this.lng = lng;
		}
	}
}

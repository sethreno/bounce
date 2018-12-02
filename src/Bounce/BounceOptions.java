package Bounce;

public class BounceOptions {

	private BounceOptions() {
	}

	public static BounceOptions GetBounceOptions() {
		return new BounceOptions();
	}

	private ScreenOptions screen;
}

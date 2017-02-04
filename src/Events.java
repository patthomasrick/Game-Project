/**
Authors:	Patrick Thomas
Date:		2/3/17
Purpose: 	This class provides some engine functions to keep track of game events, which range from timed events (the
			spawning of game objects) to onTrigger events (running into a wall, collecting a coin)
*/

import java.util.ArrayList;

public class Events
{
	// event timer keeps all timed events collected and ticking
	public static class EventTimer
	{
		// define a growning array
		ArrayList<TimedEvent> events = new ArrayList<TimedEvent>();
		
		// method to add an event to the event list
		public void add_event(TimedEvent e)
		{
			this.events.add(e);
		} // end add event
		
		// method to tick all events and check them
		public void tick()
		{
			// iterate through all current events
			// for all of those events, tick them. if they trigger, they do so on their own
			for (TimedEvent e: this.events) e.tick();
		}
	} // end EventTimer class
	
	public static class TimedEvent
	{
		// how old event is and how old can event get
		// also provides for a way to check a state every n ticks
		int age;
		int max_age;
		
		// how long event lasts
		int duration = 1;
		
		// if the event expires, should it remove itself?
		boolean repeat = false;
		
		// main output of event
		boolean triggered = false;
		
		// kill event
		boolean alive = true;
		
		// only define that event expires
		public TimedEvent(int max_age)
		{
			this.max_age = max_age;
		} // end constructor 1
		
		// only define expiration and duration
		public TimedEvent(int max_age, int duration)
		{
			this.max_age = max_age;
			this.duration = duration;
		} // end constructor 2
		
		// define expiration and repeat
		public TimedEvent(int max_age, boolean repeat)
		{
			this.max_age = max_age;
			this.repeat = repeat;
		} // end constructor 3
		
		// define all
		public TimedEvent(int max_age, int duration, boolean repeat)
		{
			this.max_age = max_age;
			this.duration = duration;
			this.repeat = repeat;
		} // end constructor 4
		
		
		// tick event
		public void tick()
		{
			if (this.alive == true)
			{
				this.age += 1;
				if (this.age >= this.max_age)
				{
					if (this.triggered == false)
					{
						this.trigger();
					}
					this.triggered = true;
					
					// run event for duration
					if (this.age >= this.max_age + this.duration)
					{
						// turn off event
						this.triggered = false;
						
						// if repeats
						if (this.repeat == true)
						{
							this.age = 0; // reset event
						} // if repeating
						else // not repeating
						{
							this.age = 0;
							this.alive = false; // kill event
						} // end if not repeatig
					} // end if very old
				} // end if old
			} // end if alive
		} // end tick
		
		public void trigger()
		{
			// more meant for custom actions other than true/false analysis
		} // end trigger
	} // end Event class
}

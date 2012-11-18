/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.utils;

/**
 *
 * @author Dur
 */
public class Fraction
{
	private long denominator;
	private long counter;
	private String message="";

	public Fraction( long denominator, long counter, String message )
	{
		this.denominator = denominator;
		this.counter = counter;
		this.message = message;
	}

	public long getCounter()
	{
		return counter;
	}

	public void setCounter( long counter )
	{
		this.counter = counter;
	}

	public long getDenominator()
	{
		return denominator;
	}

	public void setDenominator( long denominator )
	{
		this.denominator = denominator;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}
	
	public String  getCompleteMessage()
	{
		return (message + "  " + getValue() + "%"); 
	}
	
	public int getValue()
	{
		Double value = new Double( getCounter() );
		value = value / getDenominator();
		value *= 100;
		return value.intValue();
	}
}

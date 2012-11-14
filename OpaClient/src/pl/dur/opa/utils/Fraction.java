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

	public Fraction( long denominator, long counter )
	{
		this.denominator = denominator;
		this.counter = counter;
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
	
	
}

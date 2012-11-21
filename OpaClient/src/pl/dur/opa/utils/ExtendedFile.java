/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.utils;

import java.io.File;
import java.net.URI;

/**
 *
 * @author Dur
 */
public class ExtendedFile extends File
{
	private long fileCheckSum;
	
	private Long lastModifiedDate;
	
	private Long fileSize;
	
	private boolean isStored;
	
	public ExtendedFile( String pathName, Long CRC )
	{
		super( pathName );
		this.fileCheckSum = CRC.longValue();
	}
	
	public ExtendedFile( String pathname )
	{
		super( pathname );
	}
	
	public ExtendedFile(File parent, String child)
	{
		super( parent, child );
	}
	
	public ExtendedFile(String parent, String child)
	{
		super( parent, child );
	}
	
	public ExtendedFile( URI uri )
	{
		super( uri );
	}

	public long getFileCheckSum()
	{
		return fileCheckSum;
	}

	public void setFileCheckSum( long fileCheckSum )
	{
		this.fileCheckSum = fileCheckSum;
	}

	public Long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize( Long fileSize )
	{
		this.fileSize = fileSize;
	}

	public Long getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	public void setLastModifiedDate( Long lastModifiedDate )
	{
		this.lastModifiedDate = lastModifiedDate;
	}

	public boolean isIsStored()
	{
		return isStored;
	}

	public void setIsStored( boolean isStored )
	{
		this.isStored = isStored;
	}
	
	@Override
	public boolean equals(Object second )
	{
		if( ! ( second instanceof ExtendedFile ) )
		{
			return false;
		}
		else
		{
			ExtendedFile toCompare = (ExtendedFile) second; 
			if( ! (this.getName().equals( toCompare.getName() ) ) )
			{
				return false;
			}
			else
			{
				if( ( this.getFileCheckSum() != 0 ) && ( toCompare.getFileCheckSum() != 0 ) )
				{
					return this.getFileCheckSum() == toCompare.getFileCheckSum();
				}
				else
				{
					return ( ( this.length() == toCompare.length() ) && ( this.lastModified() == toCompare.lastModified() ) );
				}
			}
		}
	}
	
}

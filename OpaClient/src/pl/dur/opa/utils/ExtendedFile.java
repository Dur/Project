/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.opa.utils;

import java.io.File;
import java.net.URI;
import javax.swing.Icon;

/**
 *
 * @author Dur
 */
public class ExtendedFile extends File
{
	private long fileCheckSum;
	
	private Icon fileIcon;
	
	private boolean isConflicted;
	
	private boolean isStored;
	
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

	public Icon getFileIcon()
	{
		return fileIcon;
	}

	public void setFileIcon( Icon fileIcon )
	{
		this.fileIcon = fileIcon;
	}

	public boolean isIsConflicted()
	{
		return isConflicted;
	}

	public void setIsConflicted( boolean isConflicted )
	{
		this.isConflicted = isConflicted;
	}

	public boolean isIsStored()
	{
		return isStored;
	}

	public void setIsStored( boolean isStored )
	{
		this.isStored = isStored;
	}
	
	
	
}

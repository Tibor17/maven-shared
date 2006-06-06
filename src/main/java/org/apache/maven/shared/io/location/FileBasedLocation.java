package org.apache.maven.shared.io.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileBasedLocation
    implements Location
{

    private File file;
    private FileChannel channel;
    private final String specification;
    
    public FileBasedLocation( File file, String specification )
    {
        this.file = file;
        this.specification = specification;
    }
    
    protected FileBasedLocation( String specification )
    {
        this.specification = specification;
    }

    public void close()
    {
        if ( channel != null && channel.isOpen() )
        {
            try
            {
                channel.close();
            }
            catch ( IOException e )
            {
                //swallow it.
            }
        }        
    }

    public File getFile()
        throws IOException
    {
        initFile();
        
        return unsafeGetFile();
    }
    
    protected File unsafeGetFile()
    {
        return file;
    }

    protected void initFile()
        throws IOException
    {
        // TODO: Log this in the debug log-level...
        if ( file == null )
        {
            file = new File( specification );
        }
    }
    
    protected void setFile( File file )
    {
        if ( channel != null )
        {
            throw new IllegalStateException( "Location is already open; cannot setFile(..)." ); 
        }
        
        this.file = file;
    }

    public String getSpecification()
    {
        return specification;
    }

    public void open()
        throws IOException
    {
        initFile();
        
        channel = new FileInputStream( file ).getChannel();
    }

    public int read( ByteBuffer buffer )
        throws IOException
    {
        return channel.read( buffer );
    }

    public int read( byte[] buffer )
        throws IOException
    {
        return channel.read( ByteBuffer.wrap( buffer ) );
    }

}

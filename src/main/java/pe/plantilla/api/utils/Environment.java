package pe.plantilla.api.utils;

public final class Environment {
	
	public final static int SO_WINDOWS = 1;
	public final static int SO_LINUX = 2;
	public final static int SO_MAC = 3;
	private static int IdSistemaOperativo = 0;		
	
	private static void IdentificarSistemaOperativo () {
		String sisoper = System.getProperties().getProperty("os.name");
		if ( sisoper.toUpperCase().indexOf("WINDOWS") >= 0 ) {
			//El sistema operativo es Microsoft Windows
			IdSistemaOperativo = SO_WINDOWS;
		}
		else if ( sisoper.toUpperCase().indexOf("LINUX") >= 0 ) {
			//El sistema operativo es Linux
			IdSistemaOperativo = SO_LINUX;
		}
	}
	
	public static int getIdSistemaOperativo () {
		if ( IdSistemaOperativo == 0 )
			IdentificarSistemaOperativo();
		return IdSistemaOperativo;
	}
	
	public static String getJavaHome () {		
		return System.getProperty("java.home");
	}
	
	public static String getFileSeparator () {		
		return System.getProperty("file.separator");
	}
	
	public static String getUserDir () {		
		return System.getProperty("user.dir");
	}
        
	public static String getUserHome () {		
		return System.getProperty("user.home");
	}
	
	public static String getLibraryPath () {		
		return System.getProperty("java.library.path");
	}

}


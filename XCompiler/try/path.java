import java.io.*;
class path{
	public static void main(String[] args) {
		String path="D:/OneDrive/GeekProjects/XCompiler/sample codes/helloword.v";
		try{
			FileReader fileReader= new FileReader(path);
			StringBuffer sb=new StringBuffer();
			BufferedReader buffReader=new BufferedReader(fileReader);
			
			int nextChar=buffReader.read();
			while(nextChar!=-1){
				sb.append((char)nextChar);
				nextChar=buffReader.read();
			}
			System.out.println(sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		
	}
}
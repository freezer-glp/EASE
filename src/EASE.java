import java.io.IOException;


public class EASE
{

	
	public static void main(String[] args) throws IOException
	{
		matrix.readMtrix("file/matrix.txt", 12);
		matrix.matrixPower(2);
		
		preProcess.getRradiusGraph(matrix.poweredMatrix, matrix.matrix, 0); // 1,showlist
		preProcess.buildVertexGraphMap(0);// 1,showdebug

		fileOutput.buildKeyGraphMap("file/key1.txt", "file/map1.txt", "rw",0);
		search.doSearch("file/search.txt", "file/map1.txt","file/key1.txt", "rw",0);

	}

}

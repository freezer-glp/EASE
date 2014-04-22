import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;


class search
{

	public static RandomAccessFile sinput = null;
	public static RandomAccessFile ginput = null;
	public static RandomAccessFile vkinput = null;
	public static ArrayList<rRadiusGraph> resultList; //查询结果的r半径图
	
	public static void doSearch(String searchFilePath, String mapFilePath,String vKeyFilePath,String readmode,int debug) throws IOException
	{
		String inString;
		String temp[] = null;//暂时存放一次查询的关键词
		String keyTemp[],readbuffer = null;
		
		int tempGNum,kNum,graphCount,seekNum;
		int j1;
		
		rRadiusGraph tempGraph = null;		
		ArrayList<Integer> glist = new ArrayList<Integer>();
		resultList =  new ArrayList<rRadiusGraph>();
		
		
		Stack<Integer> s = new Stack<Integer>();
		try
		{
			sinput = new RandomAccessFile(searchFilePath, readmode);
			ginput = new RandomAccessFile(mapFilePath, readmode);
			vkinput = new RandomAccessFile(vKeyFilePath, readmode);
		}
		catch (FileNotFoundException e)
		{
			
			e.printStackTrace();
		}
		// 按行读取待查关键词
		for (inString = sinput.readLine(); inString != null; inString = sinput.readLine())
		{
			
			temp = inString.split(" ");
			System.out.print("待查关键词为：");
			for (int i = 0; i <= temp.length - 1; i++)
				System.out.print(temp[i]+" ");
			System.out.println("\n");
			glist.clear();
			resultList.clear();
			
			for (int i = 0; i <= temp.length - 1; i++) //对每一个带查关键词
			{
				kNum = Integer.parseInt(temp[i]);
				seekNum = kNum * (matrix.mSize + 1) * Integer.SIZE;
				ginput.seek(seekNum);
				graphCount = ginput.readInt();

				if (graphCount == 0)
				{
					System.out.println(kNum+"is not exist!");
					continue;
				}

				else
				{
					ginput.seek(seekNum + Integer.SIZE);//将包含该关键词的全部子图从文件中读出来
					for (int j = 1; j <= graphCount; j++)
					{

						tempGNum = ginput.readInt();
						if(!glist.contains(tempGNum))
						{
							glist.add(tempGNum);
						}			
						
					}
					
				}
				
			}
			
			//rRadiusGraph 
			for(int i = 0; i <= glist.size()-1;i++)//对所有结果图进行求并集，存于resultlist
			{
				tempGNum = glist.get(i);
				for(int j = 0 ; j <= preProcess.list.size()-1; j++)
				{
					tempGraph = preProcess.list.get(j);//将图编号对应的图从list中取出来，放入resultlist
					if(tempGraph.id == tempGNum)
					{
						resultList.add(tempGraph);
					}
				}
			}
			if(debug == 1)
				for(int i = 0;i <= resultList.size()-1 ; i++)
				{
					tempGraph = resultList.get(i);
					System.out.println(tempGraph.id);
				}
			
			//remove non-steiner node
			
			for(int i = 0;i <= resultList.size()-1 ; i++)//对每个组图
			{
				tempGraph = resultList.get(i);
				for(int j = 0; j <= matrix.mSize-1; j++)//求子图中的content node
				{
					if(tempGraph.M[j][j] == 1)
					{	
						j1 = j;
						vkinput.seek(0);
						if(debug == 1)
							System.out.println("i="+tempGraph.id+" jj="+j1);
						while(j1 != -1)
						{
							readbuffer = vkinput.readLine();
							j1--;
						}
						
						if(readbuffer == null)
							System.out.println("J="+j);
						keyTemp = readbuffer.split(" ");
						
						for(int x = 0; x <= temp.length-1 ; x++)		//如果某个顶点包含了某个关键词
							for(int y = 0; y <= keyTemp.length-1; y++)
							{
								if(keyTemp[y].equals(temp[x]) )
									tempGraph.M[j][j] = 2;				//将该顶点标记为content node
							}
						
																					
					}
				
				}//求子图中的content node
				if(debug == 1)
					for (int l = 0; l <= matrix.mSize - 1; l++)
						for (int j = 0; j <= matrix.mSize - 1; j++)
						{
							if (j != matrix.mSize - 1)
								System.out.print(tempGraph.M[l][j] + " ");
							else
								System.out.println(tempGraph.M[l][j]);
						}
				if(debug == 1)
					System.out.println("");
				
				
				for(int v = 0; v <= matrix.mSize -1 ;v++)//找出steiner顶点
				{
					if(tempGraph.M[v][v] == 2)
					{
						s.push(v);
						steinerSearch(s, tempGraph.M);//从每个content node开始，递归查找steiner node
						s.pop();
					}					
					
						
				}
				if(debug == 1)
					System.out.println("after-----------------------");
				
				
				for (int v = 0; v <= matrix.mSize - 1; v++)//去除非steiner顶点
					if (tempGraph.M[v][v] != 2 && tempGraph.M[v][v] != 3)
					{
						for (int t = 0; t <= matrix.mSize - 1; t++)
							tempGraph.M[v][t] = 0;
						for (int t = 0; t <= matrix.mSize - 1; t++)
							tempGraph.M[t][v] = 0;

					}
				
				if(debug == 1)
					for (int l = 0; l <= matrix.mSize - 1; l++)//输出看看
						for (int j = 0; j <= matrix.mSize - 1; j++)
						{
							if (j != matrix.mSize - 1)
								System.out.print(tempGraph.M[l][j] + " ");
							else
								System.out.println(tempGraph.M[l][j]);
						}
				
				tempGraph.score = ranking(tempGraph.M);//对该子图进行打分
				
				resultList.remove(i);
				resultList.add(i, tempGraph);
				
			}
			
			Collections.sort(resultList, cmp);//已经子图的得分进行排序
			
			System.out.println("result is:");
			for(int p = 0;p <= resultList.size()-1 ; p++)
			{
				System.out.println("子图编号："+resultList.get(p).id+" 得分："+resultList.get(p).score);
				for (int l = 0; l <= matrix.mSize - 1; l++)//输出看看
					for (int j = 0; j <= matrix.mSize - 1; j++)
					{
						if (j != matrix.mSize - 1)
							System.out.print(resultList.get(p).M[l][j] + " ");
						else
							System.out.println(resultList.get(p).M[l][j]);
					}
			}
			
		}//for readline
		

	}
	public static int steinerSearch(Stack<Integer> s,int m[][])//递归找出steiner顶点和content顶点
	{
		int k,find=0;
		
		k = s.peek();
		//System.out.println("stack size = "+s.size()+" k="+k);
		for(int i = 0;i <= matrix.mSize-1; i++)
		{
			if(m[k][i] != 0 && !s.contains(i))
			{
				if( m[i][i] == 2 )
					find = 1;
				else
				{
					s.push(i);
					if(steinerSearch(s,m) == 1)
					{
						find = 1;
						if(m[i][i] != 2)			//2表示该顶点为content node
						{
							
							m[i][i] = 3;			//3表示该顶点为steiner node
						}
						
					}
					s.pop();
						
				}
			}
			
				
		}
		
		return find;
	}
	
	public static double ranking(int m[][])
	{
		int cNode = 0,cSteinerNode = 0;
		double score = 0;
		
		for(int i = 0 ;i <= matrix.mSize-1; i++)
		{
			if(m[i][i] ==  2)
				cNode++;
			else if(m[i][i] == 3)
				cSteinerNode++;
			
		}
		
		score =  ((double)cNode+ (double)cSteinerNode) / (double)cNode;
		
		return score;
		
	}
	static Comparator<rRadiusGraph> cmp  = new Comparator<rRadiusGraph>()
	{

		@Override
		public int compare(rRadiusGraph o1, rRadiusGraph o2)
		{
			if(o1.score <= o2.score)
				return 1;
			else
				return -1;
		}
		
	};

}

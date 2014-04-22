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
	public static ArrayList<rRadiusGraph> resultList; //��ѯ�����r�뾶ͼ
	
	public static void doSearch(String searchFilePath, String mapFilePath,String vKeyFilePath,String readmode,int debug) throws IOException
	{
		String inString;
		String temp[] = null;//��ʱ���һ�β�ѯ�Ĺؼ���
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
		// ���ж�ȡ����ؼ���
		for (inString = sinput.readLine(); inString != null; inString = sinput.readLine())
		{
			
			temp = inString.split(" ");
			System.out.print("����ؼ���Ϊ��");
			for (int i = 0; i <= temp.length - 1; i++)
				System.out.print(temp[i]+" ");
			System.out.println("\n");
			glist.clear();
			resultList.clear();
			
			for (int i = 0; i <= temp.length - 1; i++) //��ÿһ������ؼ���
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
					ginput.seek(seekNum + Integer.SIZE);//�������ùؼ��ʵ�ȫ����ͼ���ļ��ж�����
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
			for(int i = 0; i <= glist.size()-1;i++)//�����н��ͼ�����󲢼�������resultlist
			{
				tempGNum = glist.get(i);
				for(int j = 0 ; j <= preProcess.list.size()-1; j++)
				{
					tempGraph = preProcess.list.get(j);//��ͼ��Ŷ�Ӧ��ͼ��list��ȡ����������resultlist
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
			
			for(int i = 0;i <= resultList.size()-1 ; i++)//��ÿ����ͼ
			{
				tempGraph = resultList.get(i);
				for(int j = 0; j <= matrix.mSize-1; j++)//����ͼ�е�content node
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
						
						for(int x = 0; x <= temp.length-1 ; x++)		//���ĳ�����������ĳ���ؼ���
							for(int y = 0; y <= keyTemp.length-1; y++)
							{
								if(keyTemp[y].equals(temp[x]) )
									tempGraph.M[j][j] = 2;				//���ö�����Ϊcontent node
							}
						
																					
					}
				
				}//����ͼ�е�content node
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
				
				
				for(int v = 0; v <= matrix.mSize -1 ;v++)//�ҳ�steiner����
				{
					if(tempGraph.M[v][v] == 2)
					{
						s.push(v);
						steinerSearch(s, tempGraph.M);//��ÿ��content node��ʼ���ݹ����steiner node
						s.pop();
					}					
					
						
				}
				if(debug == 1)
					System.out.println("after-----------------------");
				
				
				for (int v = 0; v <= matrix.mSize - 1; v++)//ȥ����steiner����
					if (tempGraph.M[v][v] != 2 && tempGraph.M[v][v] != 3)
					{
						for (int t = 0; t <= matrix.mSize - 1; t++)
							tempGraph.M[v][t] = 0;
						for (int t = 0; t <= matrix.mSize - 1; t++)
							tempGraph.M[t][v] = 0;

					}
				
				if(debug == 1)
					for (int l = 0; l <= matrix.mSize - 1; l++)//�������
						for (int j = 0; j <= matrix.mSize - 1; j++)
						{
							if (j != matrix.mSize - 1)
								System.out.print(tempGraph.M[l][j] + " ");
							else
								System.out.println(tempGraph.M[l][j]);
						}
				
				tempGraph.score = ranking(tempGraph.M);//�Ը���ͼ���д��
				
				resultList.remove(i);
				resultList.add(i, tempGraph);
				
			}
			
			Collections.sort(resultList, cmp);//�Ѿ���ͼ�ĵ÷ֽ�������
			
			System.out.println("result is:");
			for(int p = 0;p <= resultList.size()-1 ; p++)
			{
				System.out.println("��ͼ��ţ�"+resultList.get(p).id+" �÷֣�"+resultList.get(p).score);
				for (int l = 0; l <= matrix.mSize - 1; l++)//�������
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
	public static int steinerSearch(Stack<Integer> s,int m[][])//�ݹ��ҳ�steiner�����content����
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
						if(m[i][i] != 2)			//2��ʾ�ö���Ϊcontent node
						{
							
							m[i][i] = 3;			//3��ʾ�ö���Ϊsteiner node
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

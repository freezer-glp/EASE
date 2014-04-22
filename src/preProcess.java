import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;


class preProcess
{
	public static int copyMatrix[][];
	public static ArrayList<rRadiusGraph> list;// ����r�뾶ͼ��list
	public static Stack<Integer> s;
	public static HashMap<Integer, ArrayList<Integer>> vGraphMap;// ÿ�������������Щr�뾶ͼ��
	static rRadiusGraph rg, c1, c2, show;

	public static void getRradiusGraph(int pm[][], int m[][], int debug)//��ȡ������r�뾶��ͼ������list�У�����pmΪr���ݾ���mΪr-1���ݾ���
	{
		int find, p, num;
		copyMatrix = new int[matrix.mSize][matrix.mSize];// ԭʼ�����copy
		s = new Stack<Integer>();
		list = new ArrayList<rRadiusGraph>();

		for (int i = 0; i <= matrix.mSize - 1; i++)
		{
			s.clear();
			//find==1 ��ʾ�ҵ����Ӽ���Ԫ�أ���ͼi��r�뾶ͼ��
			for (int j = 0; j <= matrix.mSize - 1; j++)
			{
				if (pm[i][j] == 1)
					s.push(j);

			}

			find = 0;
			num = s.size();
			
			while (!s.empty())
			{

				p = s.pop();
				// System.out.println("p = "+p+" "+s.size());
				for (int j = 0; j <= matrix.mSize - 1; j++)
				{
					if (pm[i][j] == 1 && m[p][j] != 1)//��������Ӽ�
					{
						find++;
						break;
					}
				}

			}

			if (find == num)
			{
				if (debug == 1)
					System.out.println("i= " + i);
				for (int k = 0; k <= matrix.mSize - 1; k++)
					for (int j = 0; j <= matrix.mSize - 1; j++)
						copyMatrix[k][j] = m[k][j];

				for (int j = 0; j <= matrix.mSize - 1; j++)
					if (pm[i][j] == 1)
					{
						// System.out.println("j= "+j);
						s.push(j);
					}

				while (!s.empty())			//ѡ����r�뾶��ͼ
				{
					p = s.pop();
					// System.out.println("p= "+p);
					copyMatrix[p][p] = 2;
				}

				for (int j = 0; j <= matrix.mSize - 1; j++)           //��ԭͼ��ȥ������ͼ�ĵ�
					if (copyMatrix[j][j] != 2)
					{
						for (int t = 0; t <= matrix.mSize - 1; t++)
							copyMatrix[j][t] = 0;
						for (int t = 0; t <= matrix.mSize - 1; t++)
							copyMatrix[t][j] = 0;

					}

				for (int j = 0; j <= matrix.mSize - 1; j++)
					if (copyMatrix[j][j] == 2)
						copyMatrix[j][j] = 1;
				rg = null;
				rg = new rRadiusGraph(2, i, copyMatrix);
				list.add(rg);											//������ͼ����list��
				// System.out.println("size= "+list.size());

			}

		}

		removeSame();													//��list��������ͬ����ͼȥ��
		if (debug == 1)
		{

			showList();
			System.out.println("size= " + list.size());
		}

	}

	public static void removeSame()
	{
		for (int i = 0; i <= list.size() - 1; i++)
		{
			c1 = list.get(i);
			for (int j = i + 1; j <= list.size() - 1; j++)
			{
				c2 = list.get(j);
				if (isSame(c1.M, c2.M) == 1)					  //���������ͼ���ڽӾ�����ȫ��ͬ����Ϊ�ظ���
					list.remove(j);

			}
		}
	}

	public static int isSame(int m1[][], int m2[][])
	{
		int same = 1;
		for (int i = 0; i <= matrix.mSize - 1; i++)
			for (int j = 0; j <= matrix.mSize - 1; j++)
			{
				if (m1[i][j] != m2[i][j])
				{
					same = 0;
					return same;
				}
			}
		return same;
	}

	public static void showList()				//��ʾ���е�r�뾶��ͼ
	{
		for (Iterator<rRadiusGraph> it = list.iterator(); it.hasNext();)
		{
			show = it.next();
			System.out.println("id= " + show.id);
			for (int i = 0; i <= matrix.mSize - 1; i++)
				for (int j = 0; j <= matrix.mSize - 1; j++)
				{
					if (j != matrix.mSize - 1)
						System.out.print(show.M[i][j] + " ");
					else
						System.out.println(show.M[i][j]);
				}
		}
	}

	public static void buildVertexGraphMap(int debug)			//����һ������Ͱ����ö������ͼ��ӳ���
	{
		vGraphMap = new HashMap<Integer, ArrayList<Integer>>();

		for (int i = 0; i <= matrix.mSize - 1; i++)
		{

			ArrayList<Integer> tlist = new ArrayList<Integer>();
			// System.out.println(i+"before---"+tlist.size());
			if (debug == 1)
				System.out.println("vertex :" + i);
			for (Iterator<rRadiusGraph> it = list.iterator(); it.hasNext();)
			{
				rg = it.next();
				if (rg.M[i][i] == 1)
				{
					tlist.add(rg.id);
					if (debug == 1)
					{

						System.out.print(rg.id + " ");
					}
				}
			}
			if (debug == 1)
				System.out.println("");
		
			vGraphMap.put(i, tlist);
		}
		if (debug == 1)
		{
			System.out.println("from hashmap :");
			for (int i = 0; i <= vGraphMap.size() - 1; i++)
			{
				ArrayList<Integer> bufferlist = preProcess.vGraphMap.get(i);
				for (int j = 0; j <= bufferlist.size() - 1; j++)
					System.out.print(bufferlist.get(j) + " ");
				System.out.println("");
			}
		}

		// templist =

	}
}
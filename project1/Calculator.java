//1
package polynomialCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calculator {
	
	private node root;
	private Operator savedPoly ;
	private HashMap<String,Handler> handlers = new HashMap<String,Handler>();
	private ArrayList<String> variable = new ArrayList<String>();
	private boolean illegal;
	
	//构造函数
	public Calculator()
	{
		handlers.put("!simplify", new HandlerSimplify(this));
		handlers.put("!d/d", new HandlerDerivation(this));
		handlers.put("!exit",new HandlerExit(this));
		root = null;
		savedPoly = new Operator('+');
	}

	//复制树
	public node copy(node root)
	{
		node current = null;
		if(root!=null)
		{
			if(root instanceof Operator)
				current = new Operator(((Operator) root).getContent());
			else if(root instanceof Digit)
				current = new Digit(((Digit) root).getContent());
			else if(root instanceof Character)
				current = new Character(((Character) root).getContent());
				
			current.left = copy(root.left);
			current.right = copy(root.right);
		}
		return current;
	}
	
	//显示信息
	public void showPrompt()
	{
		System.out.println();
		System.out.println("请输入指令或新的表达式：");
		System.out.println("指令有: !simplify !d/d !exit");
		System.out.println();
	}
	
	//中序遍历表达式树
	public StringBuffer midTravel(node n)
	{
		StringBuffer sb = new StringBuffer();
		if(n!=null)
		{
			if((n.left==null&&n.right!=null)||(n.left!=null&&n.right==null)){
				illegal = true ;
			}
			
			//判断当前优先级，根据优先级加括号
			sb.append(midTravel(n.left));
			sb.append(n.get());
			sb.append(midTravel(n.right));
			
		}
		return sb;
		
	}
	
	//先序遍历表达式树
	public StringBuffer PreTravel(node n)
	{
		StringBuffer sb = new StringBuffer();
		if(n!=null)
		{
			if((n.left==null&&n.right!=null)||(n.left!=null&&n.right==null)){
				illegal = true ;
			}
			
			//判断当前优先级，根据优先级加括号
			sb.append(n.get());
			sb.append(PreTravel(n.left));
			sb.append(PreTravel(n.right));
			
		}
		return sb;
		
	}
	
	//打印树结构多项式
	public void print(Operator n)
	{
		if(n.getContent()=='+')
		{
			int count = 0;
			for( node k : n.son )
			{
				if(count!=0)
					System.out.print("+");
				print((Operator)k);
				count ++;
			}
		}
		else if(n.getContent()=='*')
		{
			int count = 0,factor = ((Digit)(n.son.get(0))).getContent();
			for(node k : n.son)
			{
				if(k instanceof Digit)
				{
					if(factor==1)
						continue ;
					else if(factor==-1)
						System.out.print("-");
					else{
						System.out.print(factor);
					}
				}
				else if(k instanceof Character)
				{
					if(((Character) k).getIndex()>0)
					{
						//判断是否打印乘号
						if(count!=0)
							System.out.print("*");
						else if(factor!=1&&factor!=-1)
							System.out.print("*");
							
						System.out.print(((Character) k).getContent());
						if(((Character) k).getIndex()>1)
							System.out.print("^"+((Character) k).getIndex());
						count ++;
					}
				}
			}
		}
	}
	
	//去掉括起整个表达式的括号
	public String debracket(String s)
	{
		int bracketCount = 1;
		if(s.substring(0, 1).equals("("))
			for(int i = 1 ; i < s.length() ; i++ )
			{
				if(s.substring(i, i+1).equals("("))
					bracketCount ++;
				else if(s.substring(i, i+1).equals(")"))
					bracketCount --;
				
				if(bracketCount==0)
				{
					if(i==s.length()-1)
						s = s.substring(1, s.length()-1);
					else
						break;
				}
			}
		return s;
	}

	//递归构造表达式树
	private node ContributeTree(char[] line) 
	{
		
		node current = null;
		int bracketCount = 0 , numCount = 0 , chCount = 0;
		int splitPos = 0;
		String temp = new String(line) ;
		
		//找到有括号外第一个加号
		for(int i = 0 ; i < line.length ; i++ )
		{
			if(line[i]==' ')
				continue ;
			if(line[i]=='(')
				bracketCount ++;
			else if(line[i]==')')
				bracketCount --;
			else if(line[i]=='+')
			{
				if(bracketCount==0)
				{
					splitPos = i ;
					break ;
				}
			}
			else if(line[i]>='0'&&line[i]<='9')
			{
				numCount ++ ;
			}
			else if(line[i]>='a'&&line[i]<='z')
			{
				chCount ++ ;
			}
		}
		//若括号外没哟加号，则找到有括号外第一个乘号
		if(splitPos==0&&line[0]!='+')
		{
			for(int i = 0 ; i < line.length ; i++ )
			{
				if(line[i]==' ')
					continue ;
				if(line[i]=='(')
					bracketCount ++;
				else if(line[i]==')')
					bracketCount --;
				else if(line[i]=='*')
				{
					if(bracketCount==0)
					{
						splitPos = i ;
						break ;
					}
				}
				else
					continue ;
			}
		}
		//如果该串是变量串
		if(line.length>0&&chCount==line.length)
		{
			current = new Character(line);
		}
		//如果该串是数字串
		if(line.length>0&&numCount==line.length)
		{
			int data = 0 ;
			for(int i = 0 ; i < line.length ; i++ )
			{
				data *= 10 ;
				data += line[i]-'0' ;
			}
			current = new Digit(data);
		}

		if(current instanceof Data)
		{
			current.left = null ;
			current.right = null ;
		}
		else if(line.length!=0)
		{		
			String left = temp.substring(0, splitPos);
			String right = temp.substring(splitPos+1, line.length);
			current = new Operator(line[splitPos]);
			
			if(left.equals(""))
				current.left = null;
			else
				current.left = ContributeTree(debracket(left).toCharArray());
			
			if(right.equals(""))
				current.right = null;
			else
				current.right = ContributeTree(debracket(right).toCharArray());
			
			if((current.left==null&&current.right!=null)||(current.left!=null&&current.right==null)){
				illegal = true;
			}
		}
		
		return current;
	}
	
	//预处理表达式字符数组
	public char[] preprocess(char[] line)
	{
		ArrayList process = new ArrayList();
		if(line[0]!=' ')
			process.add(line[0]);
		//增加数字-变量、变量-变量之间的乘号
		for( int i=1 ; i<line.length-1 ; i++ )
		{
			if(line[i]==' ')
			{
				if((line[i-1]>='0'&&line[i-1]<='9')&&(line[i+1]>='a'&&line[i+1]<='z'))
				{
					line[i]='*';
				}
				else if((line[i-1]>='a'&&line[i-1]<='z')&&(line[i+1]>='0'&&line[i+1]<='9'))
				{
					line[i]='*';
				}
				else if((line[i-1]>='a'&&line[i-1]<='z')&&(line[i+1]>='a'&&line[i+1]<='z'))
				{
					line[i]='*';
				}
				else
					continue;
			}
			else if((line[i]>='a'&&line[i]<='z')&&(line[i-1]>='0'&&line[i-1]<='9'))
			{
				process.add('*');
			}
			process.add(line[i]);
		}
		if(line[line.length-1]!=' ')
		{
			if((line[line.length-1]>='a'&&line[line.length-1]<='z')
					&&(line[line.length-2]>='0'&&line[line.length-2]<='9')){
				process.add('*');
			}
			process.add(line[line.length-1]);
		}
		
		char[] changed = new char[process.size()];
		for( int i=0 ; i<process.size() ; i++ )
		{
			changed[i] = (char)process.get(i);
		}
		return changed;
	}
	
	//将输入表达式存入二叉树中
	public void toTree( char[] line )
	{
		
		//预处理表达式
		line = preprocess(line);
			
		root = null ;
		int bracketCount = 0 ;
		
		//检查括号数量是否匹配、是否有非法字符
		for(int i = 0 ; i < line.length ; i++ )
		{
			if(line[i]=='(')
				bracketCount ++ ;
			else if(line[i]==')')
			{
				bracketCount -- ;
				if(bracketCount<0)
				{
					System.out.println("格式错误！");
					illegal = true ;
					break ;
				}
			}
			else if(line[i]>='0'&&line[i]<='9'||line[i]>='a'&&line[i]<='z'||line[i]=='+'||line[i]=='*')
				continue ;
			else
			{
				System.out.println("非法字符："+line[i]);
				illegal = true ;
			}
		}
		//括号数量不匹配
		if(bracketCount!=0)
		{
			System.out.println("括号不匹配！");
			illegal = true ;
		}
		
		if(!illegal){
			root = ContributeTree(line);
		}

	}	
	
	//将所有变量存入variable中
	private void getVar(node n)
	{
		if(n instanceof Operator)
		{
			getVar(n.left);
			getVar(n.right);
		}
		else if(n instanceof Character)
		{
			for( Object k : variable )
			{
				if(((Character) n).getContent().equals( (String)k ))
				{
					return ;
				}
			}
			variable.add( ((Character) n).getContent() );
		}
	}
	
	//展开多项式递归函数
	private void unfold(node n) 
	{
		if(n instanceof Operator)
		{	
			unfold(n.getLeft());
			unfold(n.getRight());
			
			if(((Operator)n).getContent()=='*')
			{	
				boolean leftIsPlus = false , rightIsPlus = false;
				
				if(n.getLeft() instanceof Operator&&((Operator)(n.getLeft())).getContent()=='+')
					leftIsPlus = true ;
				if(n.getRight() instanceof Operator&&((Operator)n.getRight()).getContent()=='+')
					rightIsPlus = true ;
				if(leftIsPlus&&rightIsPlus)
				{
					node ll = new Operator('*');
					node lr = new Operator('*');
					node rl = new Operator('*');
					node rr = new Operator('*');
					((Operator) n).set('+');
					ll.manageLeft(n.getLeft().getLeft());
					ll.manageRight(n.getRight().getLeft());
					lr.manageLeft(copy(n.getLeft().getLeft()));
					lr.manageRight(n.getRight().getRight());
					rl.manageLeft(n.getLeft().getRight());
					rl.manageRight(copy(n.getRight().getLeft()));
					rr.manageLeft(copy(n.getLeft().getRight()));
					rr.manageRight(copy(n.getRight().getRight()));
					n.left.manageLeft(ll);
					n.left.manageRight(lr);
					n.right.manageLeft(rl);
					n.right.manageRight(rr);
				}
				else if(leftIsPlus&&!rightIsPlus)
				{
					node right = new Operator('*');
					right.manageLeft(n.getLeft().getRight());
					right.manageRight(n.getRight());
					((Operator) n).set('+');
					((Operator) n.getLeft()).set('*');
					n.getLeft().manageRight(copy(n.right));
					n.manageRight(right);
				}
				else if(!leftIsPlus&&rightIsPlus)
				{
					node left = new Operator('*');
					left.manageRight(n.getRight().getLeft());
					left.manageLeft(n.getLeft());
					((Operator) n).set('+');
					((Operator) n.getRight()).set('*');
					n.getRight().manageLeft(copy(n.left));
					n.manageLeft(left);
				}				
			}
			
			unfold(n.getLeft());
			unfold(n.getRight());
	}
		
		}
	
	//将叶子结点整理好放入单项式树target中
	private void travelLeaf(node n,Operator target)
	{
		//后续遍历，若不是叶子结点则往下遍历
		if(n.left!=null&&n.right!=null)
		{
			travelLeaf(n.left,target);
			travelLeaf(n.right,target);
		}
		//如果是叶子结点
		else if(n.left==null&&n.right==null)
		{
			//数字则将当前系数乘上结点数字再存起来
			if(n instanceof Digit)
			{
				int result ;
				result = ((Digit)(target.son.get(0))).getContent()*((Digit) n).getContent();
				((Digit)(target.son.get(0))).set( result );
			}
			//若为字母则找到找到这个字母将其指数加一
			else if(n instanceof Character)
			{	
				for(node k : ((Operator)target).son )
				{
					if(k instanceof Character)
					{
						if(((Character) k).getContent().equals(((Character)n).getContent()))
						{
							((Character) k).setIndex(((Character) k).getIndex()+1);
						}
					}
					else if(k instanceof Digit)
						continue;
					
				}
			}
		}	
	}
	
	//将二叉树表达式转化为树表达式
	private node settle(node n)
	{
		//初始化乘号结点
		Operator sub = new Operator('*');
		sub.addSon(new Digit(1));
		
		//初始化乘号结点子节点
		for( Object k : variable )
		{
			node leaf = new Character((String)k);
			((Character)leaf).setIndex(0);
			sub.addSon(leaf);
		}
		
		//整理乘号结点
		travelLeaf(n,sub);
		
		return sub;
	}
	
	//将多项式合并并且存入savedPoly中
	private void clearUp(node n)
	{	
		if(n instanceof Operator&&((Operator) n).getContent()=='+')
		{
			clearUp(n.left);
			clearUp(n.right);
		}
		else if(n instanceof Data||( n instanceof Operator&&((Operator) n).getContent()=='*'))
		{
			node subnode = settle(n);
			((Operator)savedPoly).addSon(subnode);
		}
	}

	//比较两个单项式是否为同类项
	private boolean compareMono(Operator a,Operator b)
	{
		boolean isSame = true;
		for(int i = 1 ; i < a.son.size() ; i++ )
		{
			if(((Character)(a.son.get(i))).getIndex()==((Character)(b.son.get(i))).getIndex())
				continue;
			else
			{
				isSame = false;
				break;
			}
		}
		return isSame ;
	}
	
	//合并同类型
	private void combine()
	{
		//比较两个单项式是否为同类项
		for(int i = 0 ; i < savedPoly.son.size() ; i++ )
		{
			for(int j = i+1 ; j < savedPoly.son.size() ; j++ )
			{
				if(compareMono((Operator)savedPoly.son.get(i),(Operator)savedPoly.son.get(j)))
				{
					int factor = ((Digit)((Operator)savedPoly.son.get(i)).son.get(0)).getContent();
					factor += ((Digit)((Operator)savedPoly.son.get(j)).son.get(0)).getContent();
					((Digit)((Operator)savedPoly.son.get(i)).son.get(0)).set(factor);;
					savedPoly.son.remove(j);
					j--;
				}
			}
		}
	}
	
	//将表达式化简存入树中
	private void Save(char[] input) 
	{
		
		//初始化两棵树
		root = null;
		savedPoly = new Operator('+');
		variable.clear();
		
		//将表达式存入树中
		toTree(input);

		if(!illegal)
		{
			//存储所有变量
			getVar(root);
			
			//根据树将表达式全部展开（去括号）
			unfold(root);
		
			//化简多项式
			clearUp(root);
						
			//合并同类项
			combine();
			
			print(savedPoly);
		}
	}
	//

	
	//化简表达式指令
	public void simplify(String cmd)
	{
		ArrayList<String> input = new ArrayList<String>();
		HashMap<Integer,Integer> pair = new HashMap<Integer,Integer>();
		String[] cut = cmd.split(" ");
		boolean exist = false;
		
		//将输入字符串中的变量与其对应值存到哈希表中
		for(int i=0 ; i < cut.length ; i++ )
		{
			exist = false;
			if(!cut[i].equals(""))
			{
				String[] divide = cut[i].split("="); 
				if(divide.length!=2)
				{
					illegal = true;
					return;
				}
				else
				{
					int value = 0;
					char[] data = divide[1].toCharArray();
					for(int j = 0 ; j < data.length ; j++)
					{
						if(data[j]>='0'&&data[j]<='9')
						{
							value *= 10;
							value += data[j]-'0';
						}
						else
						{
							illegal = true;
							return;
						}
					}
					
					//判断该变量是否存在
					for(String k : variable)
					{
						if(k.equals(divide[0]))
						{
							exist = true;
							input.add(k);
							pair.put(variable.indexOf(k)+1, value);
							break;
						}
					}
					if(!exist)
					{
						System.out.println("变量"+divide[0]+"不存在！");
					}
				}
			}
		}
		
		//将值赋给每个单项式
		for( node n : savedPoly.son )
		{
			int factor = ((Digit)(((Operator)n).son.get(0))).getContent();
			for( Integer k : pair.keySet() )
			{
				Character point = (Character)((Operator)n).son.get(k);
				for( int i = 0 ; i < point.getIndex() ; i++ )
				{
					factor *= (int)pair.get(k);
				}
				((Digit)((Operator)n).son.get(0)).set(factor);
			}
			//如果该项值为0，则移除
			if(factor==0)
			{
				savedPoly.son.remove(n);
			}
			//移除赋值的变量
			else
			{
				for(String k : input)
				{
					for(node q : ((Operator)n).son)
					{
						if(q instanceof Character&&k.equals(((Character) q).getContent()))
						{
							((Operator)n).son.remove(q);
							break;
						}
					}
				}
			}
		}
		
		//移除变量表中赋值的变量
		for( String k : input )
		{
			for( String p : variable )
			{
				if(k.equals(p))
				{
					variable.remove(p);
					break;
				}
			}
		}
		
		combine();
		print(savedPoly);
		
	}
	//
	
	
	//求导指令
	public void derivation(String cmd)
	{
		int j = 0 , pos =0;
		char[] var = cmd.toCharArray(); 
		for( int i = 0 ; i < var.length ; i++,j++ )
		{
			if(j>i&&var[i]!=' ')
			{
				illegal = true;
				break;
			}
			else if(var[i]==' ')
				continue;
			else
			{
				j = i;
				for( j = i ; j < var.length ; j++ )
				{
					if(var[j]==' ')
						break;
				}
				String cut = new String(var).substring(i, j);
				illegal = true;
				for(String k:variable)
				{
					if(!(k.equals(cut)))
						continue;
					else
					{
						pos = variable.indexOf(k);
						pos ++;
						illegal = false;
						break;
					}
				}
				if(!illegal){
					break;
				}
			}
		}
		
		//将值赋给每个单项式
		if(!illegal)
		{
			for( int i=0 ; i < savedPoly.son.size() ; i++ )
			{
				node n = savedPoly.son.get(i);
				int factor = ((Digit)(((Operator)n).son.get(0))).getContent();
				int index = ((Character)(((Operator)n).son.get(pos))).getIndex();
				factor *= index;
				((Character)(((Operator)n).son.get(pos))).setIndex(index-1);
				((Digit)(((Operator)n).son.get(0))).set(factor);
					
				//如果该项值为0，则移除
				if(factor==0)
				{
					savedPoly.son.remove(n);
					i--;
				}
				//否则移除赋值的变量
				else
				{
					((Operator)n).son.remove(pos);
				}
				
			}	
			combine();
			print(savedPoly);
		}
		illegal = false;
		
	}
	//

	
	//计算主方法
	public void calculate()
	{
		
		Scanner s = new Scanner(System.in);
		
		while(true)
		{
			showPrompt();
			String line = s.nextLine();
			line = line.toLowerCase();
			char[] input = line.toCharArray();
			
			//有待用handler格式化代码
			if(input[0]!='!')
			{
				Save(input);
			}
			else
			{
				Handler handler = null;
				
				if(line.length()>4&&line.subSequence(0, 4).equals("!d/d"))
					handler = handlers.get("!d/d");
				else if(line.length()>4&&line.subSequence(0, 5).equals("!exit"))
					handler = handlers.get("!exit");
				else if(line.length()>9&&line.subSequence(0, 9).equals("!simplify"))
					handler = handlers.get("!simplify");
				else
					illegal = true;
				
				if(handler!=null)
				{
					if(handler.isExit())
						break;
					else if(root!=null)
						handler.doCmd(line);
					else
						System.out.println("没有创建表达式！");
				}
				else
				{
					System.out.println("Error,illeagal input!");
				}
			}
			
			if(illegal){
				System.out.println("不合法输入！");
			}

			illegal = false ;
		}
	}

	public static void main(String[] args) {
		
		Calculator c = new Calculator();
	
		c.calculate();
		
		System.out.println("Bye");
	}

}
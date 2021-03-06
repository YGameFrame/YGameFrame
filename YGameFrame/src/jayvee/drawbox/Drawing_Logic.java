package jayvee.drawbox;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import jayvee.cow.CowLogic;

import android.os.Message;

import fyy.ygame_frame.base.YABaseDomainLogic;
import fyy.ygame_frame.base.YDrawInformation.YDrawInfoForm;
import fyy.ygame_frame.extra.YITask;
import fyy.ygame_frame.extra.YRequest;

public class Drawing_Logic extends YABaseDomainLogic<Drawing_Data> {

	public Queue<Boxs> dq = new LinkedList<Boxs>();
	private CowLogic cl;
	private int xoffset = -5;// 每次箱子位移的距离
	private float fRate = 0.5f;// 创建箱子时该箱子不会被显示的概率

	private Boxs tempbox = dq.peek();//临时的箱子类

	public Drawing_Logic(Drawing_Data domainData , CowLogic cl) {
		super(domainData);
		// TODO Auto-generated constructor stub
		// for (int queue_count = 0; queue_count < 2; queue_count++) {
		this.cl = cl;
		Boxs b = new Boxs(fRate);
		dq.add(b);
		// }
	}

	@Override
	protected void onSendRequests(
			Map<Integer, YABaseDomainLogic<?>> domainLogics) {
		// TODO Auto-generated method stub
		 Message msg = Message.obtain();
		 YRequest yr = new YRequest(0, 789, cl);
		 yr.objExtra = tempbox;
		 msg.obj = yr;
		 handlerGameLogic.sendMessage(msg);
		

	}
	

	YITask drawtask = new YITask() {
		int newcount = 0;

		public void execute(YDrawInfoForm drawInfoForm,
				Map<Integer, YABaseDomainLogic<?>> domainLogics) {
			// TODO Auto-generated method stub
			int queue_count = dq.size();
			for (int i = 0; i < queue_count; i++) {

				
//				if (dq.peek().iX > 700)// 超出了屏幕，此处600须修改（适配后）
//				{
//					dq.poll();
//					queue_count -=1;
//					// dq.add(new Boxs(fRate));
//					continue;
//				} else 
					if (dq.peek().iX == 600) {
//					dq.add(new Boxs(fRate));
					newcount++;//需要新建箱子的计数，一轮循环后应该可以计算出需要新建多少箱子
				}
				dq.peek().addX(xoffset);// 增加偏移
				tempbox = dq.poll();
				dq.add(tempbox);// 将队尾元素取出并重新塞入队头；
			}//理论上每轮循环完毕以后，队尾元素都是存活时间最久的那个箱子
			if(dq.peek().iX<-50)
			{
				dq.poll();
			}
			while (newcount>0)
			{
				dq.add(new Boxs(fRate));
				newcount--;
			}//每轮以后都新建了正确个数的箱子，并最后newcount=0
			tempbox = dq.peek();
			// 一轮结束后tempbox指向队列最开始的一个对象
			drawInfoForm.iArg1 = tempbox.iX;// 尝试着将tempbox的位置画出来
			drawInfoForm.iArg2 = tempbox.iY;
			drawInfoForm.objExtra = dq;

		}
	};

	@Override
	protected YITask[] onSubmitCurrentTasks() {
		// TODO Auto-generated method stub
		return new YITask[] { drawtask };
	}

	@Override
	protected int onDealRequest(YRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}

	// public Boxs getQhead(){
	// return
	// }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zhuaqu.ali1688.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import net.sf.json.JSONObject;

import com.ydj.common.dao.DaoFactory;
import com.ydj.simpleSpider.MyLog;


/**
 * 
 * @author : Ares.yi
 * @createTime : Aug 17, 2012 1:49:22 PM
 * @version : 1.0
 * @description :
 */
public class CateSpiderJFrame extends javax.swing.JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	/**
     * Creates new form NewJFrame
     */
    public CateSpiderJFrame() {
        initComponents();
    }
    
    
    private Timer timer = new Timer();
    
    private void refresh(){
    	 
    	 
    	 jLabel_msg.setText("抓取成功数："+SpiderAli1688.getSum());

         
	}
    
    private void schedule(){
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				refresh();
			}
		}, 1000 * 1, 1000 * 1);
	}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initComponents() {

    	this.setTitle("抓 抓 抓...抓个蛋蛋----分类抓取");
    	
        jLabel_startURL = new javax.swing.JLabel();
		jTextField_startURL = new javax.swing.JTextField();
		
        jLabel_keyword = new javax.swing.JLabel();
        jTextField_keyword = new javax.swing.JTextField();
        
        
        jLabel_iuCode = new javax.swing.JLabel();
        jCoboBox_iuCode1 = new javax.swing.JComboBox();
        jCoboBox_iuCode2 = new javax.swing.JComboBox();
        
        jLabel_frequencySet = new javax.swing.JLabel();
        jTextField_frequencySet = new javax.swing.JComboBox();
        
        
        jLabel_msg = new  javax.swing.JLabel();
        
        
        jButton_start = new javax.swing.JButton();
        jButton_exit = new javax.swing.JButton();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jLabel_keyword.setText("关键词");
        jLabel_startURL.setText("入口URL");
        jLabel_iuCode.setText("对应行业");
        jLabel_frequencySet.setText("抓取频率");
        jLabel_msg.setText("");
        
       
        jLabel_keyword.setBounds(50, 50, 50, 30);
        jTextField_keyword.setBounds(110, 50, 100, 30);
        
        jLabel_startURL.setBounds(50, 100, 50, 30);
        jTextField_startURL.setBounds(110, 100, 400, 30);
        
        
        jLabel_iuCode.setBounds(50, 150, 50, 30);
        jCoboBox_iuCode1.setBounds(110, 150, 120, 30);
        jCoboBox_iuCode2.setBounds(250, 150, 120, 30);
        
        
        jLabel_frequencySet.setBounds(50, 200, 50, 30);
        jTextField_frequencySet.setBounds(110, 200, 120, 30);
        
        jLabel_msg.setBounds(110, 250, 500, 30);
        jLabel_msg.setForeground(Color.red);
        
        jButton_start.setBounds(110, 290, 100, 30);
        jButton_exit.setBounds(350, 290, 100, 30);
        
        
        
        
        jButton_start.setText("开始抓取");
        jButton_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectJButtonActionPerformed(evt);
            }
        });
        
        jButton_exit.setText("退       出");
        jButton_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();
            }
        });
        
        
        jCoboBox_iuCode1.setRenderer(new MyCellRenderer());
        jCoboBox_iuCode2.setRenderer(new MyCellRenderer());
        
        List<Industry> oneList= IndustryNewCache.getOneIndustry(); 
        jCoboBox_iuCode1.addItem(new Industry("",""));
        for(Industry one : oneList){
        	jCoboBox_iuCode1.addItem(one);
        }
        
        
        jCoboBox_iuCode1.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					
					if(jCoboBox_iuCode1.getSelectedItem() != null){
						String key = ((Industry)jCoboBox_iuCode1.getSelectedItem()).key;
						MyLog.logInfo(key);
						if(Toolbox.isNotEmpty(key)){
							jCoboBox_iuCode2.addItem(new Industry("",""));
						     	for(Industry one : IndustryNewCache.getSecondIndustry(key)){
						        	jCoboBox_iuCode2.addItem(one);
						        }
						}
					}
					
				}
				
			}});
        
        
        
        Object items2[] = new Object[]{"600毫秒~1.8秒","1秒~3秒"}; 
        jTextField_frequencySet.setModel(new javax.swing.DefaultComboBoxModel(items2));
        
        pack();
        
        
        Container cont = getContentPane();
        
        JPanel pan1 = new JPanel();
        pan1.setLocation(200, 200);
        pan1.setLayout(null);//注意此处的null
        
        pan1.add(jLabel_keyword);
        pan1.add(jTextField_keyword);
        
        pan1.add(jLabel_startURL);
        pan1.add(jTextField_startURL);
        
        pan1.add(jLabel_iuCode);
        pan1.add(jCoboBox_iuCode1);
        pan1.add(jCoboBox_iuCode2);
        
        
        pan1.add(jLabel_frequencySet);
        pan1.add(jTextField_frequencySet);
        
        pan1.add(jLabel_msg);
        
        pan1.add(jButton_start);
        pan1.add(jButton_exit);
        
        
//        pan1.setBackground(Color.GRAY);
        
        cont.add(pan1, BorderLayout.CENTER);
        

        
        this.setIconImage(Toolkit.getDefaultToolkit().createImage(CateSpiderJFrame.class.getResource("logo.png"))); 
        this.setResizable(false);//设置不可以最大化
        this.setLocationRelativeTo(null);
        
        this.setSize(600, 400);
    }
    
    

    private void connectJButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	final String keyword = this.jTextField_keyword.getText();
    	final String startURL = this.jTextField_startURL.getText();
    	final Object iuCode2 = jCoboBox_iuCode2.getSelectedItem();
    	Object frequencySet = jTextField_frequencySet.getSelectedItem();
    	
    	MyLog.logInfo("keyword: "+keyword);
    	MyLog.logInfo("startURL: "+startURL);
    	MyLog.logInfo("frequencySet: "+frequencySet.toString());
    	
    	String msg = "";
    	if(Toolbox.isEmptyString(keyword)){
    		msg += "  请填写关键词" ;
    	}
    	
    	if(Toolbox.isEmptyString(startURL) || !startURL.startsWith("https://s.1688.com/") || startURL.lastIndexOf("beginPage=") < 0){
    		JOptionPane.showMessageDialog(null, "入口URL必须以【https://s.1688.com/】开始和【beginPage=】结束","提示",JOptionPane.ERROR_MESSAGE); 
    		return ;
    	}
    	
    	
    	if(iuCode2 == null || Toolbox.isEmptyString(((Industry)iuCode2).value)){
    		msg += "  请选择正确的对应行业" ;
    	}
    	
    	if(Toolbox.isNotEmpty(msg)){
    		jLabel_msg.setText(msg);
    		return ;
    	}
    	
    	//"600毫秒~1.8秒","1秒~3秒"
    	
    	int min = 600,max = 1800;
    	if(frequencySet.toString().equals("1秒~3秒")){
    		min = 1000;
    		max = 3 * 1000;
    	}
    	
    	final int minTime = min,maxTime=max;
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				businessProcess(keyword, startURL, ((Industry)iuCode2).value, minTime, maxTime);
			}
		}).start();
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    	
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CateSpiderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CateSpiderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CateSpiderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CateSpiderJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    	
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CateSpiderJFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_keyword;
    private javax.swing.JLabel jLabel_startURL;
    private javax.swing.JLabel jLabel_iuCode;
    private javax.swing.JLabel jLabel_frequencySet;
    
    private javax.swing.JLabel jLabel_msg;

    private javax.swing.JTextField jTextField_keyword;
    private javax.swing.JTextField jTextField_startURL;
    @SuppressWarnings("rawtypes")
	private javax.swing.JComboBox jCoboBox_iuCode1;
    @SuppressWarnings("rawtypes")
   	private javax.swing.JComboBox jCoboBox_iuCode2;
    @SuppressWarnings("rawtypes")
	private javax.swing.JComboBox jTextField_frequencySet;
    
    private javax.swing.JButton jButton_start;
    private javax.swing.JButton jButton_exit;


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    // End of variables declaration//GEN-END:variables
	
	
	
	
	class MyCellRenderer extends JLabel implements ListCellRenderer<Object>{
		
		private static final long serialVersionUID = 1L;

		MyCellRenderer(){
	         setOpaque(true);
	    }
	     
	     public Component getListCellRendererComponent(JList<?> list,
	                                                  Object value,
	                                                  int index,
	                                                  boolean isSelected,
	                                                  boolean cellHasFocus)
	    {
	        if (value != null)
	         {
	             setText(((Industry)value).value);
//	             setIcon(((ItemObj)value).icon);
	        }
	
	         if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        }
	         else {
	             setBackground(list.getBackground());
	             setForeground(list.getForeground());
	         }
	
	       return this;
	     }    
	 }
	
	
	
	private void businessProcess(String keyword,String startURL,String iuCode,int min,int max){
		
		JSONObject json = DaoFactory.getMyDao().getStore(startURL);

		if(json != null){
			this.jLabel_msg.setText("入口URL已经存在");
			return ;
		}
		
		DaoFactory.getMyDao().saveStore(keyword, startURL, iuCode);
		
		json = DaoFactory.getMyDao().getStore(startURL);
		
		if(json == null){
			return ;
		}
		 
		 this.jButton_start.setEnabled(false);
		 
		 schedule();
		 
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i=1 ;i<101;i++){
			list.add(i);
		}
		
		Collections.shuffle(list);//尽量打乱翻页顺序
		
		int typeOf = json.getInt("id");
		
		for(int page : list){
			SpiderAli1688.getStoreInfo(typeOf, startURL+page, page,min,max);
		}
		
		int spiderCount = SpiderAli1688.getSum();
		
		DaoFactory.getMyDao().updateStore(startURL, spiderCount);
		
		SpiderAli1688.makeZero();
		
		this.jButton_start.setEnabled(true);
	}
	
}

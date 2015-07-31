/**
 * Copyright (c) 2005 tomtom@kuroneko 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jp.gr.java_conf.boj.lib.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

/**
 * JComponentのenabledプロパティはコンポーネントの操作を無効に
 * できるがコンポーネントによって見た目も変更される。<br>
 * このコンポーネントは設定されているJComponentを透明なカバーで
 * 覆うことで見た目はそのままでJComponentに対する操作を無効に
 * する事ができる。<br>
 * 操作とはアプリケーションユーザによるマウスアクションや
 * フォーカス移動、ドラッグアンドドロップを指す。<br>
 * 透明なカバーを掛ける対象となるJComponentは１つしか設定できないが
 * そのJComponentがコンテナとしていくつコンポーネントを格納していても
 * かまわない。<br>
 */
public class CoveredPane extends JPanel implements MouseListener,
                                                   MouseMotionListener {
	
	/**
	 * 透明なカバーとなるJPanel<br>
	 * この透明なパネルがマウスイベントなどを消費する事で背後の
	 * JCompoenentに対する操作を無効にする。<br>
	 * サブクラスはこのクラスのコンストラクタで独自のJPanelを
	 * 透明カバーとして設定可能だがその場合にはそのJPanel自身が
	 * マウスアクションの処理やドラッグアンドドロップに対する
	 * 処理を実装している必要がある。<br>
	 * フォーカス移動を無効にする処理に関してはCoveredPaneが
	 * 強制的に行うので透明カバーとなるJPanelが意識する必要はない。
	 */
	protected final JPanel transparentCover;
	
	/**
	 * 透明カバーで覆う対象のJComponentオブジェクト。<br>
	 * セットされていなければnull<br>
	 */
	protected JComponent target;
	
	/**
	 * 透明カバーで覆っていればtrue
	 */
	protected boolean covered;
	
	// 使い回す。コンストラクタ以外では各フィールドの変更はない。
	private GridBagConstraints gbc = new GridBagConstraints();
	
	/**
	 * デフォルトコンストラクタ
	 */
	public CoveredPane() {
		this(null);
	}
	
	/**
	 * 透明なカバーに対するマウスアクションの処理を行いたい場合や
	 * 透明なカバーの表面に描画を行いたい場合にはこのクラスで
	 * 空実装しているpaintTransparentCoverメソッドや、mouseClicked、
	 * mouseMovedなどのマウスイベントを処理するメソッドを
	 * サブクラスでオーバーライドする。<br>
	 * それらのオーバーライドだけでは不十分な場合にはサブクラスからは
	 * このコンストラクタでtransparentCoverを指定できる。<br>
	 * 引数がnullではない場合にはサブクラスの作成者が引数のJPanelによる
	 * マウスアクションの処理やドラッグアンドドロップに対する
	 * 処理の実装に関して責任を持たなければならない。<br>
	 * フォーカス移動を無効にする処理に関してはCoveredPaneが
	 * 強制的に行うので引数のJPanelが意識する必要はない。<br>
	 * 引数がnullではない場合にこのクラスで空実装している
	 * paintTransparentCoverメソッドやMouseListener, 
	 * MouseMotionListenerのメソッドをサブクラスでオーバーライドしても
	 * 無意味となる。
	 * 
	 * @param transparentCover 透明カバーとして機能するJPanelオブジェクト。
	 *                         nullの場合にはこのクラスで用意した
	 *                         デフォルトのJPanelが使用される。
	 */
	protected CoveredPane(JPanel transparentCover) {
		if (transparentCover == null) {
			this.transparentCover = new TransparentCover();
		} else {
			this.transparentCover = transparentCover;
		}
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.1; gbc.weighty = 0.1;
	}
	
	/**
	 * 透明カバーで覆う対象のJComponentオブジェクトを設定する。<br>
	 * 現在設定されているJComponentオブジェクトがあれば破棄して
	 * 引数のJComponentオブジェクトを設定する。<br>
	 * 引数を設定するだけでカバーで覆ってはいない状態となる。<br>
	 * 引数がnullの場合にはIllegalArgumentExceptionをスローする。
	 * 
	 * @param target セットするJComponentオブジェクト。
	 * @exception IllegalArgumentException 引数がnullの場合
	 */
	public void setTarget(JComponent target) {
		if (target == null) {
			throw new IllegalArgumentException("argument is null !");
		}
		if (this.target != null) {
			removeAll();
		}
		
		this.target = target;
		covered = false;
		add(target, gbc);
		setFocusCycleRoot(false);
		revalidate();
		repaint();
	}
	
	/**
	 * 透明カバーで覆う対象のJComponentオブジェクトを返す。<br>
	 * 設定されていなければnullを返す。
	 * 
	 * @return カバーで覆う対象のJComponentオブジェクト。
	 *         設定されていなければnullを返す。
	 */
	public JComponent getTarget() {
		return target;
	}
	
	/**
	 * 透明カバーで覆う対象のJComponentオブジェクトが設定されていれば
	 * 破棄してそのJComponentオブジェクト返す。<br>
	 * 設定されていなければ何もせずにnullを返す。<br>
	 * 
	 * @return 設定されているJComponentオブジェクトを破棄して返す。
	 *         設定されていなければnullを返す。
	 */
	public JComponent removeTarget() {
		if (target == null) {
			return null;
		}
		
		//カバーがあればカバーも破棄
		removeAll();
		setFocusCycleRoot(false);
		revalidate();
		repaint();
		return target;
	}
	
	/**
	 * coverメソッドにより透明カバーで覆っていればtrueを返す。<br>
	 * 対象となるJComponentオブジェクトが設定されていない場合には
	 * カバーの状態に関わらずIllegalStateExceptionをスローする。
	 * 
	 * @return coverメソッドで透明なカバーをしていればtrue
	 * @exception IllegalStateException 透明カバーを掛ける対象の
	 *            JComponentオブジェクトがセットされていない場合
	 */
	public boolean isCovered() {
		if (target == null) {
			throw new IllegalStateException("target is nothing !");
		}
		return covered;
	}
	
	/**
	 * 透明カバーで覆う対象のJComponentオブジェクトが設定されていれば
	 * trueを返し、設定されていなければfalseを返す。
	 * 
	 * @return 透明カバーで覆う対象のJComponentオブジェクトが
	 *         設定されていればtrue
	 */
	public boolean hasTarget() {
		return (target != null);
	}
	
	/**
	 * 設定されているJComponentオブジェクトを透明カバーで覆い、
	 * アプリケーションユーザによるマウスアクションやフォーカス移動、
	 * ドラッグアンドドロップを無効にする。<br>
	 * すでにカバーで覆っている状態の場合には何もしない。<br>
	 * 対象のJComponentオブジェクトが設定されていない場合には
	 * カバーの状態に関わらずIllegalStateExceptionをスローする。
	 * 
	 * @exception IllegalStateException 透明カバーで覆う対象の
	 *            JComponentオブジェクトがセットされていない場合
	 */
	public void cover() {
		if (target == null) {
			throw new IllegalStateException("target is nothing !");
		}
		if (!covered) {
			remove(target);
			add(transparentCover, gbc);
			add(target, gbc);
			setFocusCycleRoot(true);
			setFocusable(false);
			covered = true;
			revalidate();
			repaint();
		}
	}
	
	/**
	 * 透明カバーを外して設定されているJComponentに対する
	 * アプリケーションユーザによる操作を有効にする。<br>
	 * すでにカバーをはずしている状態の場合には何もしない。<br>
	 * 中身のJComponentオブジェクトが設定されていない場合には
	 * カバーの状態に関わらずIllegalStateExceptionをスローする。
	 * 
	 * @exception IllegalStateException 透明カバーで覆う対象の
	 *            JComponentオブジェクトが設定されていない場合
	 */
	public void uncover() {
		if (target == null) {
			throw new IllegalStateException("target is nothing !");
		}
		if (covered) {
			remove(transparentCover);
			setFocusCycleRoot(false);
			covered = false;
			revalidate();
			repaint();
		}
	}
	
	/** 
	 * transparentCoverにaddしたMouseListener#mouseClickedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 * */
	public void mouseClicked(MouseEvent e) {}
	
	/** 
	 * transparentCoverにaddしたMouseListenermousePressedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mousePressed(MouseEvent e) {}

	/** 
	 * transparentCoverにaddしたMouseListener#mouseReleasedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mouseReleased(MouseEvent e) {}

	/** 
	 * transparentCoverにaddしたMouseListener#mouseEnteredの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mouseEntered(MouseEvent e) {}

	/** 
	 * transparentCoverにaddしたMouseListener#mouseExitedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mouseExited(MouseEvent e) {}

	/** 
	 * transparentCoverにaddしたMouseMotionListener#mouseDraggedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mouseDragged(MouseEvent e) {}

	/** 
	 * transparentCoverにaddしたMouseMotionListener#mouseMovedの空実装。<br>
	 * 必要ならサブクラスでオーバーライドする。
	 */
	public void mouseMoved(MouseEvent e) {}
	
	/**
	 * このインスタンスをCoveredPaneクラスのデフォルトコンストラクタを使って
	 * 生成した場合にはtransparentCoverに対する描画をこのメソッドに
	 * 実装できる。<br>
	 * 例えば完全な透明ではなく半透明にしたい場合には半透明の
	 * (α値を設定した)Colorオブジェクトを g に設定してから
	 * (0, 0, transparentCover.getWidth(), transparentCover.getHeight()) 
	 * の範囲を g.fillRect で塗りつぶしたり、ポップアップメニューなど
	 * のように背後のコンポーネントに関する何らかの情報を描画するなど。<br>
	 * このメソッドはtransparentCoverのpaintComponentメソッドにおいて
	 * super.paintComponent(g) を呼び出した後に呼び出される。<br>
	 * 必要な場合にサブクラスでオーバーライドする。
	 * 
	 * @param g transparentCoverに描画するためのグラフィックスコンテキスト
	 */
	protected void paintTransparentCover(Graphics g) {}
	
	/* 透明カバーとして機能するJPanel */
	private class TransparentCover extends JPanel {
		TransparentCover() {
			setOpaque(false);
			
			// デフォルトのサイズを幅0、高さ0にする為(FlowLayoutではダメ)
			setLayout(new GridBagLayout());
			
			// MouseEventを消費
			addMouseListener(CoveredPane.this);
			
			// MouseMotionEventを消費
			addMouseMotionListener(CoveredPane.this);
			
			// ドラッグ&ドロップを消費
			setTransferHandler(new TransferHandler(null));
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			paintTransparentCover(g);
		}
	}
}

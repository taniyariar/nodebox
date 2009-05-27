package net.nodebox.client;

import net.nodebox.client.editor.SimpleEditor;
import net.nodebox.node.Node;
import net.nodebox.node.NodeCode;
import net.nodebox.node.Parameter;
import net.nodebox.node.PythonCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditorPane extends Pane {

    private PaneHeader paneHeader;
    private SimpleEditor editor;
    //private CodeArea codeArea;
    private Node node;

    public EditorPane(NodeBoxDocument document) {
        this();
        setDocument(document);
    }


    public EditorPane() {
        setLayout(new BorderLayout(0, 0));
        paneHeader = new PaneHeader(this);
        JButton reloadButton = new JButton(new ReloadAction());
        reloadButton.setSize(53, 21);
        reloadButton.setForeground(SwingUtils.normalColor);
        reloadButton.setBorderPainted(false);
        reloadButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        reloadButton.setMargin(new Insets(0, 0, 0, 0));
        reloadButton.setFont(SwingUtils.boldFont);
        paneHeader.add(reloadButton);
        editor = new SimpleEditor();
        CodeArea.defaultInputMap.put(PlatformUtils.getKeyStroke(KeyEvent.VK_R), new ReloadAction());
        add(paneHeader, BorderLayout.NORTH);
        add(editor, BorderLayout.CENTER);
    }

    @Override
    public void setDocument(NodeBoxDocument document) {
        super.setDocument(document);
        if (document == null) return;
        setNode(document.getActiveNode());
    }

    public Pane clone() {
        return new EditorPane(getDocument());
    }

    public String getPaneName() {
        return "Code";
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        if (this.node == node && node != null) return;
        this.node = node;
        Parameter pCode = null;
        if (node != null)
            pCode = node.getParameter("_code");
        if (pCode == null) {
            editor.setSource("");
            editor.setEnabled(false);
        } else {
            String code = pCode.asCode().getSource();
            editor.setSource(code);
            editor.setEnabled(true);
        }
    }

    @Override
    public void focusedNodeChanged(Node activeNode) {
        setNode(activeNode);
    }

    private class ReloadAction extends AbstractAction {
        private ReloadAction() {
            super("Reload");
            ImageIcon icon = new ImageIcon("res/code-reload.png", "Reload");
            putValue(LARGE_ICON_KEY, icon);
        }

        public void actionPerformed(ActionEvent e) {
            if (node == null) return;
            Parameter pCode = node.getParameter("_code");
            if (pCode == null) return;
            NodeCode code = new PythonCode(editor.getSource());
            pCode.set(code);
        }
    }
}

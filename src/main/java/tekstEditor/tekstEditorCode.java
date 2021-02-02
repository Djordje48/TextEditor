/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tekstEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;


/**
 *
 * @author abc
 */
public class tekstEditorCode extends javax.swing.JFrame {
   
    String imeprograma="Tekst Editor";
    String imefajla="";
    String sadrzitekst;
    String ep;
    String direktorijum;
    boolean promenautekstu=false;
    
    

    /**
     * Creates new form tekstEditorCode
     */
    public tekstEditorCode() {
        initComponents();
        
        //pozivanje fje za lepljenje
        Action zalepi = new DefaultEditorKit.PasteAction();
        zalepiOpMeni2.addActionListener(zalepi);
        //pozivanje fje za kopiranje
        Action kopiraj = new DefaultEditorKit.CopyAction();
        kopirajOpMeni2.addActionListener(kopiraj);
        // pozivznje fje za isecanje
         Action iseci = new DefaultEditorKit.CutAction();
         iseciOpMeni2.addActionListener(iseci);
         //pozivanje fje za selektovanje 
         izaberiSveOpMeni2.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                tekstPro.selectAll();
            }
        });
         //pozivanje fje za undo i redo
          UndoManager menadzer = new UndoManager();
         tekstPro.getDocument().addUndoableEditListener(menadzer);
         ponisti(menadzer);
         ponovi(menadzer);
         ponistiOpMeni2.setEnabled(false);
          tekstPro.addCaretListener(new CaretListener() {
            
            public void caretUpdate(CaretEvent e) {

                if (menadzer.canUndo()) {
                    ponistiOpMeni2.setEnabled(true);
                } else {
                    ponistiOpMeni2.setEnabled(false);
                }
                
            }
        });
         //na pocetku programa ne mozemo vrstiti funkcije napred i nazad,kopiraj,iseci(jer nema nikakvog teksta)
         ponoviOpMeni2.setEnabled(false);
          tekstPro.addCaretListener(new CaretListener() {
           
            public void caretUpdate(CaretEvent e) {

               
                if (menadzer.canRedo()) {
                    ponoviOpMeni2.setEnabled(true);
                } else {
                    ponoviOpMeni2.setEnabled(false);
                }
            }
        });
         //pozivanje fje za pronalazenje pojma u tekstu
         pronadjiOpMeni2.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
               
                jDialog1.setVisible(true);
                jDialog1.setSize(600,400);
                dugmeTraz.setEnabled(false);

             
                proveriTekstPretragu();
                pronadji();
                zatvoriPretragu();
            }
        });
        kopirajOpMeni2.setEnabled(false);
        iseciOpMeni2.setEnabled(false);
        pronadjiOpMeni2.setEnabled(false);
        zameniOpMeni2.setEnabled(false);

        
        tekstPro.addCaretListener(new CaretListener() {
            
            public void caretUpdate(CaretEvent e) {
                String trenutniTekst = tekstPro.getText();
                
                if (trenutniTekst.length() != 0) {
                    pronadjiOpMeni2.setEnabled(true);
                    zameniOpMeni2.setEnabled(true);
                }
                
                if (tekstPro.getSelectedText() != null) {
                    iseciOpMeni2.setEnabled(true);
                    kopirajOpMeni2.setEnabled(true);
                } else {
                    iseciOpMeni2.setEnabled(false);
                    kopirajOpMeni2.setEnabled(false);
                }

            }
        });
        //pozivanje fje za zamenu
         zameniOpMeni2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                jDialog2.setVisible(true);
                jDialog2.setSize(600,400);
                dugmeZameni1.setEnabled(false);
                dugmeZameni2.setEnabled(false);

               
                proveriTekstZamena();
                zameni();
                zameniSve();
                zatvoriZamenu();
            }
        });  
         //pozivanje fje za font
         promeniFontOpMeni2.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                jFrame1.setVisible(true);
                jFrame1.setSize(600,400);
                
                postaviPromenuFonta();
                postaviPromenuFontaForma();
                promeniFont();
                promeniVelicinu();
                promeniStil();
                klikDugmicaPromenaFonta();
            }
        });
       
         
        
    }
    
  
    public void proveriFajl(){
        BufferedReader procitaj;
        StringBuffer stringB= new StringBuffer();
        try{
            procitaj=new BufferedReader(new FileReader(imefajla));
            String linija;
            while((linija=procitaj.readLine())!= null){
                stringB.append(linija+"\n");
            }
            tekstPro.setText(stringB.toString());
            procitaj.close();
        }catch (FileNotFoundException e){
            System.out.println("Fajl nije pronađen");
            
        }catch (IOException ioe){
        
        }
    }
     private void sacuvajKao() {
        FileDialog fajld=new FileDialog(tekstEditorCode.this,"Sačuvaj",FileDialog.SAVE);
        fajld.show();
        if(fajld.getFile()!= null){
            ep=fajld.getFile();
            direktorijum=fajld.getDirectory();
            imefajla=direktorijum+ep+".txt";
            
            setTitle(imefajla);
            try
            {
                DataOutputStream podaci=new DataOutputStream(new FileOutputStream(imefajla));
                sadrzitekst=tekstPro.getText();
                BufferedReader baferr=new BufferedReader(new StringReader(sadrzitekst));
                while((sadrzitekst=baferr.readLine())!= null)
                {
                    podaci.writeBytes(sadrzitekst+"\r\n");
                    podaci.close();
                    
                }
            }
                catch (Exception e)
                {
                            System.out.println("Dati fajl nije pronađen");
                        
                }
                tekstPro.requestFocus();
                sacuvaj(imefajla);
            }
        }
     private void sacuvaj(String imefajla){
         setTitle(imeprograma+" "+imefajla);
         try
         {
             FileWriter out;
             out=new FileWriter(ep);
             out.write(tekstPro.getText());
             out.close();
         }
         catch (Exception error)
         {
             System.out.println("Greška: "+ error);
         }
         promenautekstu=false;
         sacuvajOpMeni.setEnabled(false);
         
     }
    
     private void otvoriNovo()  {
        
         
         
         if(tekstPro.getText().length() <1 ){
             setTitle("Bez naslova-"+imeprograma);
             tekstPro.setText("");
             promenautekstu=false;
         }
         else if (!promenautekstu){
             setTitle("Bez naslova-"+imeprograma);
             tekstPro.setText("");
             promenautekstu=false;
         }
         else{
             int potvrda=JOptionPane.showConfirmDialog(null,"Da li želite da sačuvate pre nego što zatvorite program?");
             if (potvrda==JOptionPane.YES_OPTION)
             {
                 if("".equals(imefajla)){
                     sacuvajKao();
                 }
                 else{
                     sacuvaj(imefajla);
                 }
                 setTitle(imeprograma);
                 imefajla="";
                 tekstPro.setText("");
                 promenautekstu=false;
             }    
             else if(potvrda==JOptionPane.NO_OPTION){
                 setTitle(imeprograma);
                 tekstPro.setText("");
                 promenautekstu=false;
             }
         }
     }
     private void otvori(){
         if(tekstPro.getText().length()<1){
            FileDialog fajld=new FileDialog(this,"Izaberi fajl",FileDialog.LOAD);
            fajld.show();
            if(fajld.getFile()!=null){
                imefajla=fajld.getDirectory()+fajld.getFile();
                setTitle(imefajla);
                proveriFajl();
            }
            tekstPro.requestFocus();
        }
        else if(!promenautekstu){
            FileDialog fajld =new FileDialog(this,"Izaberi fajl",FileDialog.LOAD);
            fajld.show();
            if(fajld.getFile()!=null){
                imefajla=fajld.getDirectory()+fajld.getFile();
                setTitle(imefajla);
                proveriFajl();
            }
            tekstPro.requestFocus();
        }
        else{
            int potvrda=JOptionPane.showConfirmDialog(null,"Da li želite da sačuvate pre nego što zatvorite program?");
            if(potvrda==JOptionPane.YES_OPTION)
            {
                if("".equals(imefajla)){
                    sacuvajKao();
                }
                else{
                    sacuvaj(imefajla);
                }
                FileDialog fajld= new FileDialog (this,"Izaberi fajl",FileDialog.LOAD);
                fajld.show();
                if(fajld.getFile()!=null){
                    imefajla=fajld.getDirectory()+fajld.getFile();
                    setTitle(imefajla); 
                    proveriFajl();
                }
                tekstPro.requestFocus();
            }
            else if(potvrda==JOptionPane.NO_OPTION){
                FileDialog fajld= new FileDialog (this,"Izaberi fajl",FileDialog.LOAD);
                fajld.show();
                if(fajld.getFile()!=null){
                    imefajla=fajld.getDirectory()+fajld.getFile();
                    setTitle(imefajla);
                    proveriFajl();
                     
                }
                tekstPro.requestFocus();
            }
        }
         
     }
     private void izadji(){
         if("".equals(tekstPro.getText())){
             System.exit(0);
         }
         else if(!promenautekstu){
             System.exit(0);
         }
         else{
             int potvrda=JOptionPane.showConfirmDialog(this,"Da li želite da sačuvate pre nego što zatvorite program?");
             if(potvrda==JOptionPane.YES_OPTION){
                 if(imefajla.equals(""))
                     sacuvajKao();
                 else
                     sacuvaj(imefajla);
             }
             if(potvrda==JOptionPane.NO_OPTION)
             {
                 System.exit(0);
             }
             
         }
     }
     private void izaberiBoju(){
         Color boja=setBoja.showDialog(null,"Set boja",setBoja.getColor());
         tekstPro.setForeground(boja);
     }
     
   
    
     private void ponisti(UndoManager menadzer){
       
                
         ponistiOpMeni2.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                menadzer.undo();
            }
         });
     }
     private void ponovi(UndoManager menadzer){
          ponoviOpMeni2.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                menadzer.redo();
            }
        });
     }
     
     private void pronadji() {
        //dugme trazi,ugasi ako nema teksta
        dugmeTraz.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
                String pronadjiTekst = pojamTraz.getText();
                int trenutna;
                int indeksTrazenja = -1;
                
                //smer trazenja
                if (radioDugmeTraz2.isSelected()) {
                   
                    trenutna = tekstPro.getSelectionEnd();
                    indeksTrazenja = tekstPro.getText().indexOf(pronadjiTekst, trenutna);
                } else {
                    try {
                        trenutna = tekstPro.getSelectionStart();
                        String textCurrentCheck =tekstPro.getText(0, trenutna);
                        indeksTrazenja = textCurrentCheck.lastIndexOf(pronadjiTekst);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
                // da li ima teksta za trazenje
                if (indeksTrazenja != -1) {
                    tekstPro.setSelectionStart(indeksTrazenja);
                    tekstPro.setSelectionEnd(indeksTrazenja + pronadjiTekst.length());
                } else {
                    JOptionPane.showMessageDialog(jDialog1, "Traženi pojam nije pronađen \"" + pronadjiTekst + "\"", "Rezultat", 2);
                }
            }
        });
    }
     
     public void zatvoriPretragu() {
        dugmeTrazIzlaz.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                jDialog1.setVisible(false);
            }
        });
    }
     
     public void proveriTekstPretragu() {
        pojamTraz.addCaretListener(new CaretListener() {
           
            public void caretUpdate(CaretEvent e) {
                if (pojamTraz.getText().trim().isEmpty()) {
                    dugmeTraz.setEnabled(false);
                } else {
                    dugmeTraz.setEnabled(true);
                }
            }
        });
    }
     public void zameni() {
        dugmeZameni1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String trenutniTekst = tekstPro.getText();
                String tekstPronadji = pojamZameni1.getText();
                String tekstZameni = pojamZameni2.getText();
                tekstPro.setText(trenutniTekst.replaceFirst(tekstPronadji, tekstZameni));
                int trenutniIndeks = tekstPro.getText().lastIndexOf(tekstZameni) + tekstZameni.length();

                int indeksTrazenja = -1;
                indeksTrazenja = tekstPro.getText().indexOf(tekstPronadji, trenutniIndeks);
              
                if (indeksTrazenja != -1) {
                    tekstPro.setSelectionStart(indeksTrazenja);
                    tekstPro.setSelectionEnd(indeksTrazenja + tekstPronadji.length());
                } else {
                    JOptionPane.showMessageDialog(jDialog2, "Traženi pojam nije pronađen \"" + tekstPronadji + "\"", "Rezultat", 2);
                }
            }
        });
    }
     public void zameniSve() {
        dugmeZameni2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (tekstPro.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(jDialog2, "Traženi pojam nije pronađen", "Greška", 2);
                } else {
                    String trenutniTekst = tekstPro.getText();
                    String tekstPronadji= pojamZameni1.getText();
                    String tekstZameni = pojamZameni2.getText();
                    tekstPro.setText(trenutniTekst.replaceAll(tekstPronadji, tekstZameni));
                }
            }
        });
    }
       public void proveriTekstZamena() {
        pojamZameni1.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (pojamZameni1.getText().trim().isEmpty()) {
                    dugmeZameni1.setEnabled(false);
                    dugmeZameni2.setEnabled(false);
                } else {
                    dugmeZameni1.setEnabled(true);
                    dugmeZameni2.setEnabled(true);
                }
            }
        });
        
    }
       public void zatvoriZamenu() {
        dugmeZameniIzlaz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialog2.setVisible(false);
            }
        });
    }
        public void postaviPromenuFontaForma() {
        pregled.setPreferredSize(new Dimension(150, 50));
        jFrame1.setTitle("Font");
        String font[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        listaFont.setListData(font);
        String stil[] = {"Regular", "Bold", "Italic", "Bold Italic"};
        listaStil.setListData(stil);
        String velicina[] = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "36", "40", "72"};
        listaVelicina.setListData(velicina);
    }
         public void postaviPromenuFonta() {
        String fontTrenutna = tekstPro.getFont().getFamily();
        int stilTrenutna = tekstPro.getFont().getStyle();
        int velicinaTrenutna = tekstPro.getFont().getSize();
        listaFont.setSelectedValue(fontTrenutna, true);
        listaStil.setSelectedIndex(stilTrenutna);
        listaVelicina.setSelectedValue(Integer.toString(velicinaTrenutna), true);
        tekstVelicina.setText(Integer.toString(velicinaTrenutna));
        tekstFont.setText(fontTrenutna);
        tekstStil.setText(listaStil.getSelectedValue());
    }
     public void promeniFont() {
        listaFont.addListSelectionListener(new ListSelectionListener() {
          
            public void valueChanged(ListSelectionEvent e) {
                String fontIzaberi = listaFont.getSelectedValue();
                int stilTrenutna = pregledTeksta.getFont().getStyle();
                int velicinaTrenutna = pregledTeksta.getFont().getSize();
                tekstFont.setText(fontIzaberi);
                pregledTeksta.setFont(new Font(fontIzaberi, stilTrenutna, velicinaTrenutna));
            }
        });
    }
      public void promeniStil() {
        listaStil.addListSelectionListener(new ListSelectionListener() {
           
            public void valueChanged(ListSelectionEvent e) {
                int izaberiStil = listaStil.getSelectedIndex();
                String fontTrenutna = pregledTeksta.getFont().getFontName();
                int velicinaTrenutna = pregledTeksta.getFont().getSize();
                switch (izaberiStil) {
                    case 0:
                        tekstStil.setText("Regular");
                        break;
                    case 1:
                        tekstStil.setText("Bold");
                        break;
                    case 2:
                        tekstStil.setText("Italic");
                        break;
                    case 3:
                        tekstStil.setText("Bold Italic");
                        break;
                }
                pregledTeksta.setFont(new Font(fontTrenutna, izaberiStil, velicinaTrenutna));
            }
        });
    }
       public void promeniVelicinu() {
        listaVelicina.addListSelectionListener(new ListSelectionListener() {
            
            public void valueChanged(ListSelectionEvent e) {
                String izaberiVelicinu = listaVelicina.getSelectedValue();
                String fontTrenutna = pregledTeksta.getFont().getFontName();
                int stilTrenutna = pregledTeksta.getFont().getStyle();
                tekstVelicina.setText(izaberiVelicinu);
                pregledTeksta.setFont(new Font(fontTrenutna, stilTrenutna, Integer.parseInt(izaberiVelicinu)));
            }
        });
         tekstVelicina.addCaretListener(new CaretListener() {
            
            public void caretUpdate(CaretEvent e) {
                String izaberiVelicinu = tekstVelicina.getText();
                if (izaberiVelicinu == "") {
                    String fontTrenutna = pregledTeksta.getFont().getFontName();
                    int stilTrenutna =pregledTeksta.getFont().getStyle();
                    pregledTeksta.setFont(new Font(fontTrenutna, stilTrenutna, Integer.parseInt(izaberiVelicinu)));
                }
            }
        });
    }
        public void klikDugmicaPromenaFonta() {
        dugmeOk.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
                String fontTrenutna = listaFont.getSelectedValue();
                int stilTrenutna = listaStil.getSelectedIndex();
                int velicinaTrenutna = Integer.parseInt(tekstVelicina.getText());
                tekstPro.setFont(new Font(fontTrenutna, stilTrenutna, velicinaTrenutna));
                jFrame1.setVisible(false);
            }
        });
        dugmeCancel.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                jFrame1.setVisible(false);
            }
        });
    }
     
       
      
       
  
       
        
    
    
     
        
     
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogBoje = new javax.swing.JDialog();
        setBoja = new javax.swing.JColorChooser();
        jDialog1 = new javax.swing.JDialog();
        dugmeTraz = new javax.swing.JButton();
        dugmeTrazIzlaz = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        pojamTraz = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        radioDugmeTraz2 = new javax.swing.JRadioButton();
        radioDugmeTraz1 = new javax.swing.JRadioButton();
        jDialog2 = new javax.swing.JDialog();
        dugmeZameni1 = new javax.swing.JButton();
        dugmeZameniIzlaz = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        pojamZameni1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        pojamZameni2 = new javax.swing.JTextField();
        dugmeZameni2 = new javax.swing.JButton();
        jFrame1 = new javax.swing.JFrame();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tekstFont = new javax.swing.JTextField();
        tekstStil = new javax.swing.JTextField();
        tekstVelicina = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaFont = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaStil = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaVelicina = new javax.swing.JList<>();
        pregled = new javax.swing.JScrollPane();
        pregledTeksta = new javax.swing.JLabel();
        dugmeOk = new javax.swing.JButton();
        dugmeCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tekstPro = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        novoOpMeni = new javax.swing.JMenuItem();
        otvoriOpMeni = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        sacuvajOpMeni = new javax.swing.JMenuItem();
        sacuvajKaoOpMeni = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        izadjiOpMeni = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        ponistiOpMeni2 = new javax.swing.JMenuItem();
        ponoviOpMeni2 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        iseciOpMeni2 = new javax.swing.JMenuItem();
        kopirajOpMeni2 = new javax.swing.JMenuItem();
        zalepiOpMeni2 = new javax.swing.JMenuItem();
        izaberiSveOpMeni2 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        pronadjiOpMeni2 = new javax.swing.JMenuItem();
        zameniOpMeni2 = new javax.swing.JMenuItem();
        promeniFontOpMeni2 = new javax.swing.JMenuItem();
        promeniBojuOpMeni2 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout dialogBojeLayout = new javax.swing.GroupLayout(dialogBoje.getContentPane());
        dialogBoje.getContentPane().setLayout(dialogBojeLayout);
        dialogBojeLayout.setHorizontalGroup(
            dialogBojeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogBojeLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(setBoja, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        dialogBojeLayout.setVerticalGroup(
            dialogBojeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogBojeLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(setBoja, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        dugmeTraz.setText("Pronađi tekst:");

        dugmeTrazIzlaz.setText("Izađi");

        jLabel1.setText("Traženi pojam:");

        pojamTraz.setText("Tekst:");

        jLabel2.setText("Smer:");

        radioDugmeTraz2.setText("Dole");

        radioDugmeTraz1.setText("Gore");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(radioDugmeTraz1)
                .addGap(44, 44, 44)
                .addComponent(radioDugmeTraz2)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioDugmeTraz2)
                    .addComponent(radioDugmeTraz1))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pojamTraz))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addContainerGap(322, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(dugmeTraz)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(dugmeTrazIzlaz)
                        .addGap(51, 51, 51))))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dugmeTraz)
                    .addComponent(jLabel1)
                    .addComponent(pojamTraz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(dugmeTrazIzlaz)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );

        dugmeZameni1.setText("Pronađi tekst");

        dugmeZameniIzlaz.setText("Izađi");

        jLabel3.setText("Traženi pojam:");

        pojamZameni1.setText("Tekst:");

        jLabel5.setText("Zameni pojam:");

        pojamZameni2.setText("Tekst:");

        dugmeZameni2.setText("Zameni sve");

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pojamZameni1)
                    .addComponent(pojamZameni2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dugmeZameni1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dugmeZameni2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dugmeZameniIzlaz)
                .addGap(92, 92, 92))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dugmeZameni1)
                    .addComponent(jLabel3)
                    .addComponent(pojamZameni1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(pojamZameni2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dugmeZameni2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(dugmeZameniIzlaz)
                .addGap(117, 117, 117))
        );

        jLabel4.setText("Font:");

        jLabel6.setText("Stil fonta:");

        jLabel7.setText("Velicina:");

        listaFont.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listaFont);

        listaStil.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listaStil);

        listaVelicina.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(listaVelicina);

        pregledTeksta.setText("AaBbYyZz");
        pregled.setViewportView(pregledTeksta);

        dugmeOk.setText("U redu");

        dugmeCancel.setText("Izađi");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(103, 103, 103)
                        .addComponent(jLabel6)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel7)
                        .addGap(0, 71, Short.MAX_VALUE))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(tekstFont, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                                    .addComponent(tekstStil))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4)
                                    .addComponent(tekstVelicina)))
                            .addComponent(pregled)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(dugmeOk)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dugmeCancel)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tekstFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tekstStil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tekstVelicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pregled, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dugmeOk)
                    .addComponent(dugmeCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Tekst Editor v1.0");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tekstPro.setColumns(20);
        tekstPro.setRows(5);
        tekstPro.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tekstProPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tekstPro);

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setToolTipText("");
        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("Fajl");

        novoOpMeni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        novoOpMeni.setText("Novo");
        novoOpMeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novoOpMeniActionPerformed(evt);
            }
        });
        jMenu1.add(novoOpMeni);

        otvoriOpMeni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        otvoriOpMeni.setText("Otvori");
        otvoriOpMeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otvoriOpMeniActionPerformed(evt);
            }
        });
        jMenu1.add(otvoriOpMeni);
        jMenu1.add(jSeparator1);

        sacuvajOpMeni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        sacuvajOpMeni.setText("Sačuvaj");
        sacuvajOpMeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sacuvajOpMeniActionPerformed(evt);
            }
        });
        jMenu1.add(sacuvajOpMeni);

        sacuvajKaoOpMeni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        sacuvajKaoOpMeni.setText("Sačuvaj kao");
        sacuvajKaoOpMeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sacuvajKaoOpMeniActionPerformed(evt);
            }
        });
        jMenu1.add(sacuvajKaoOpMeni);
        jMenu1.add(jSeparator2);

        izadjiOpMeni.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        izadjiOpMeni.setText("Izađi");
        izadjiOpMeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                izadjiOpMeniActionPerformed(evt);
            }
        });
        jMenu1.add(izadjiOpMeni);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Promena");

        ponistiOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        ponistiOpMeni2.setText("Poništi");
        jMenu2.add(ponistiOpMeni2);

        ponoviOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        ponoviOpMeni2.setText("Ponovi");
        jMenu2.add(ponoviOpMeni2);
        jMenu2.add(jSeparator3);

        iseciOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.SHIFT_MASK));
        iseciOpMeni2.setText("Iseci");
        jMenu2.add(iseciOpMeni2);

        kopirajOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        kopirajOpMeni2.setText("Kopiraj");
        jMenu2.add(kopirajOpMeni2);

        zalepiOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        zalepiOpMeni2.setText("Zalepi");
        jMenu2.add(zalepiOpMeni2);

        izaberiSveOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        izaberiSveOpMeni2.setText("Izaberi sve");
        jMenu2.add(izaberiSveOpMeni2);
        jMenu2.add(jSeparator4);

        pronadjiOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        pronadjiOpMeni2.setText("Pronađi");
        jMenu2.add(pronadjiOpMeni2);

        zameniOpMeni2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        zameniOpMeni2.setText("Zameni");
        jMenu2.add(zameniOpMeni2);

        promeniFontOpMeni2.setText("Promeni font");
        jMenu2.add(promeniFontOpMeni2);

        promeniBojuOpMeni2.setText("Promeni boju ");
        promeniBojuOpMeni2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                promeniBojuOpMeni2ActionPerformed(evt);
            }
        });
        jMenu2.add(promeniBojuOpMeni2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//pozivanje fja preko dugmica
    private void novoOpMeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novoOpMeniActionPerformed
      
            otvoriNovo();
       
        
    }//GEN-LAST:event_novoOpMeniActionPerformed

    private void sacuvajKaoOpMeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sacuvajKaoOpMeniActionPerformed
        
            
            sacuvajKao();
        
    }//GEN-LAST:event_sacuvajKaoOpMeniActionPerformed

    private void sacuvajOpMeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sacuvajOpMeniActionPerformed
        
        if(imefajla.equals(""))
        {
             
                sacuvajKao();
            
        }
        else
        {
            sacuvaj(imefajla);
        }
    }//GEN-LAST:event_sacuvajOpMeniActionPerformed

    private void otvoriOpMeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otvoriOpMeniActionPerformed
       otvori();
    }//GEN-LAST:event_otvoriOpMeniActionPerformed

    private void izadjiOpMeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_izadjiOpMeniActionPerformed
        izadji();
    }//GEN-LAST:event_izadjiOpMeniActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        izadji();
    }//GEN-LAST:event_formWindowClosing

    private void promeniBojuOpMeni2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_promeniBojuOpMeni2ActionPerformed
        izaberiBoju();
    }//GEN-LAST:event_promeniBojuOpMeni2ActionPerformed

    private void tekstProPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tekstProPropertyChange
        // TODO add your handling code here:
          if(TextEvent.TEXT_VALUE_CHANGED!=0)
        {
            if(!promenautekstu)
                setTitle("* "+getTitle());
            promenautekstu=true;
            sacuvajOpMeni.setEnabled(true);
            
            
        }
    }//GEN-LAST:event_tekstProPropertyChange
 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tekstEditorCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tekstEditorCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tekstEditorCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tekstEditorCode.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tekstEditorCode().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog dialogBoje;
    private javax.swing.JButton dugmeCancel;
    private javax.swing.JButton dugmeOk;
    private javax.swing.JButton dugmeTraz;
    private javax.swing.JButton dugmeTrazIzlaz;
    private javax.swing.JButton dugmeZameni1;
    private javax.swing.JButton dugmeZameni2;
    private javax.swing.JButton dugmeZameniIzlaz;
    private javax.swing.JMenuItem iseciOpMeni2;
    private javax.swing.JMenuItem izaberiSveOpMeni2;
    private javax.swing.JMenuItem izadjiOpMeni;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JMenuItem kopirajOpMeni2;
    private javax.swing.JList<String> listaFont;
    private javax.swing.JList<String> listaStil;
    private javax.swing.JList<String> listaVelicina;
    private javax.swing.JMenuItem novoOpMeni;
    private javax.swing.JMenuItem otvoriOpMeni;
    private javax.swing.JTextField pojamTraz;
    private javax.swing.JTextField pojamZameni1;
    private javax.swing.JTextField pojamZameni2;
    private javax.swing.JMenuItem ponistiOpMeni2;
    private javax.swing.JMenuItem ponoviOpMeni2;
    private javax.swing.JScrollPane pregled;
    private javax.swing.JLabel pregledTeksta;
    private javax.swing.JMenuItem promeniBojuOpMeni2;
    private javax.swing.JMenuItem promeniFontOpMeni2;
    private javax.swing.JMenuItem pronadjiOpMeni2;
    private javax.swing.JRadioButton radioDugmeTraz1;
    private javax.swing.JRadioButton radioDugmeTraz2;
    private javax.swing.JMenuItem sacuvajKaoOpMeni;
    private javax.swing.JMenuItem sacuvajOpMeni;
    private javax.swing.JColorChooser setBoja;
    private javax.swing.JTextField tekstFont;
    private javax.swing.JTextArea tekstPro;
    private javax.swing.JTextField tekstStil;
    private javax.swing.JTextField tekstVelicina;
    private javax.swing.JMenuItem zalepiOpMeni2;
    private javax.swing.JMenuItem zameniOpMeni2;
    // End of variables declaration//GEN-END:variables

   
   
}

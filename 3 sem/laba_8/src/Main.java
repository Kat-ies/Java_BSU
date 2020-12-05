import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Tree<Integer> myTree = new Tree<Integer>();
        Tree<Person> pTree = new Tree<Person>();
        File inputPerson = new File("src\\input2.txt");
        File input = new File("src\\input.txt");

        try {
            Scanner scan = new Scanner(input);
            while (scan.hasNext()) {
                myTree.insert(scan.nextInt());
            }
            System.out.println("root-left-right path:");
            myTree.lkr();
            System.out.println();
            int a = 5;
            if (myTree.find(a))
                System.out.println("Elem " + a + " is found");
            else
                System.out.println("Elem " + a + " isn't found");
            if (myTree.insert(a))
                System.out.println("Elem " + a + " inserted");
            else
                System.out.println("Elem " + a + " not inserted");
            a = 10;
            if (myTree.remove(a))
                System.out.println("Elem " + a + " removed");
            else
                System.out.println("Elem " + a + " not removed");
            System.out.println("left-root-right path:");
            myTree.klr();
            System.out.println();
            System.out.println("left-right-root path:");
            myTree.lrk();
            System.out.println();
            System.out.println();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound!");
        } catch (InputMismatchException e) {
            System.out.println("Wrong data!");
        } catch (NoSuchElementException ex) {
            System.out.println("Problems with elems!");
        } catch (NullPointerException ex) {
            System.out.println("error!");
        }
        try {
            Scanner scanNew = new Scanner(inputPerson);
            StringBuffer fio = new StringBuffer();
            int id;
            while (scanNew.hasNextInt()) {
                id = scanNew.nextInt();
                fio.append(scanNew.nextLine());
                pTree.insert(new Person(id, fio.toString()));
                fio.delete(0, fio.length());
            }
            pTree.lkr();
             pTree.insert(new Person(8, "Иванов Иван Иванович\n"));
            pTree.insert(new Person(4, "Буй Василий Иванович\n"));
            pTree.insert(new Person(14, "Козловская Елена Михайловна\n"));
            pTree.insert(new Person(13, "Юрковская Екатерина Артуровна\n"));
            pTree.lkr();

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound!");
        } catch (InputMismatchException e) {
            System.out.println("Wrong data!");
        } catch (NoSuchElementException ex) {
            System.out.println("Problems with elems!");
        } catch (NullPointerException ex) {
            System.out.println("error!");
        }
    }
}

class Tree<T extends Comparable<T>> {
    class Node {
        T info;
        Node left;
        Node right;
       Node father;

        Node(T x) {
            info = x;
          //  left = right  = null;
          left = right = father = null;
        }

        Node() {
           // left = right  = null;
           left = right = father = null;
        }

        public Node getRSon() {
            return right;
        }  //getters

        public Node getLSon() {
            return left;
        }


        public T getInfo() {
            return info;
        }
    }

    Node root;

    Tree() {
        root = null;
    }

    Tree(T x) {
        root = new Node(x);
    }

    public int size() {
        return size(root);
    }  //размер

    public int size(Node position) {
        if (position == null)
            return 0;
        return size(position.left) + size(position.right) + 1;
    }

    public boolean find(T x) { //поиск вершины
        Node current = root;
        while (current != null) {
            if (current.info.compareTo(x) == 0)
                return true;
            if (current.info.compareTo(x) < 0) {
                if (current.right == null)
                    return false;
                current = current.right;
            } else {
                if (current.left == null)
                    return false;
                current = current.left;
            }
        }
        return (false);
    }


    public boolean insert(T x) {

        if (find(x))
            return false;

        if (root == null) {
            Node p = new Node();
            p.info = x;
            root = p;
            return true;
        } else {
            Node p = root;
            Node q = new Node();
            q.info = x;
            for (; ; ) {
                if (p.info.compareTo(x) > 0) {
                    if (p.left == null) {
                        p.left = q;
                        q.father = p;
                        return true;
                    } else {
                        p = p.left;
                    }
                } else {
                    if (p.right == null) {
                         q.father = p;
                        p.right = q;
                        return true;
                    } else {
                        p = p.right;
                    }
                }
            }
        }
    }

    private Node makeNode(T x) {
        Node current = root;
        Node n = new Node();
        while (current.info.compareTo(x) != 0) {
            if (current.info.compareTo(x) < 0) {
                if (current.right != null)
                    current = current.right;
            } else {
                if (current.left != null)
                    current = current.left;
            }
        }
        n.info = x;
        n.right = current.right;
        n.left = current.left;
        n.father = current.father;
        return n;
    }

   //удаление значения
    public boolean remove(T e) {
        Node s;
        Node q;
        if (!find(e))
            return false;
        else {
            s = makeNode(e);
            if ((s.left != null) && (s.right != null)) {
                q = s.right;
                while (q.left != null)
                    q = q.left;
                s.info = q.info;
                eraseTr(q);
            } else
                eraseTr(s);
            return true;
        }
    }

    //удаление эл-та дерева
    private void eraseTr(Node s) {
        Node q = new Node();
        if (s.left != null)
            q = s.left;
        else
            q = s.right;
        if (q != null)
            q.father = s.father;
        if (s.father == null)
            root = q;
        else if (s.father.left == s)
            s.father.left = q;
        else
            s.father.right = q;
        s = null;
    }

    // Обход дерева «Корень-Левый-Правый»; klr
//  Обход дерева «Левый-Правый-Корень»; lrk
// Обход дерева «Левый-Корень-Правый». lkr
    public void lkr() {
        lkr(root);
    }

    public void lrk() {
        lrk(root);
    }

    public void klr() {
        klr(root);
    }

    private void lkr(Node localRoot) {
        if (localRoot != null) {
            lkr(localRoot.left);
            System.out.print(localRoot.getInfo().toString() + " ");
            lkr(localRoot.right);
        }
    }

    private void lrk(Node localRoot) {
        if (localRoot != null) {
            lrk(localRoot.left);
            lrk(localRoot.right);
            System.out.print(localRoot.getInfo().toString() + " ");
        }
    }

    private void klr(Node localRoot) {
        if (localRoot != null) {
            System.out.print(localRoot.getInfo().toString() + " ");
            klr(localRoot.left);
            klr(localRoot.right);
        }
    }
}

class Person implements Comparable<Person> {
    private int id;
    private String fio;

    public Person(int i, String str) {
        id = i;
        fio = str;
    }

    public Person() {
        id = 0;
        fio = "noname";
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ID: " + Integer.toString(id) + " FIO: " + fio);
        return buf.toString();
    }

    public int compareTo(Person p) {
        return Integer.compare(id, p.id);
    }
}

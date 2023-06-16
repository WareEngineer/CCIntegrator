package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.w3c.dom.Element;

import com.github.javaparser.JavaToken;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;

import generation.model.Model;
import io.FileExplorer;
import io.MyFileReader;
import migration.merging.SPLrepository;

public class Analyzer {

	public static void analyze(String dir) {
		int LOC = 0;
		int files = 0;
		int emptyFiles = 0;
		for(String path : FileExplorer.collectJavaFiles(dir)) {
			files++;
			File file = new File(path);
			CompilationUnit cu = null;
			try {
				cu = StaticJavaParser.parse(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TokenRange range = cu.getTokenRange().get();
			StringBuffer buffer = new StringBuffer();
			for(JavaToken token : range) {
				switch(token.getKind()) {
				case 5: buffer.append("\n"); break;
				case 8: buffer.append(" ");  break;
				case 9: buffer.append(" ");  break;
				default: 
					buffer.append(token.getText());
				}
			}
			int loc = 0;
			String code = buffer.toString();
			for(String line : code.split("\n")) {
				String tl = line.trim();
				if(tl.isEmpty()) continue;
				loc++;
			}
			LOC += loc;
			if(loc == 0) emptyFiles++;
		}
		System.out.println("Files:"+files+", EmptyFiles:"+emptyFiles+", LOC:"+LOC);
	}
	
	public static void analyzeAssetRepository() {
		Model model = new Model(SPLrepository.MODEL);
		List<String> products = model.getProductList();
		List<Element> elements = model.getElementsByTagName("asset");
		int total = elements.size();
		int unique = 0;
		int shared = 0;
		int common = 0;
		int platform = 0;
		int deterministic = 0;
		int non_deterministic = 0;
		int VP = 0;
		int CV = 0;
		int CL = 0;
		int SL = 0;
		int UL = 0;
		String SINGLE = "1";
		String ALL = Integer.toString(products.size());
		for(Element e : elements) {
			String count = e.getAttribute("count");
			if(count.equals(SINGLE)) {
				unique++;
			} else if(count.equals(ALL)) {
				common++;
				if(e.getAttribute("has_the_same_relative_path").equals("true")) {
					platform++;
					deterministic++;
				} else {
					non_deterministic++;
				}
			} else {
				shared++;
				if(e.getAttribute("has_the_same_relative_path").equals("true")) {
					deterministic++;
				} else {
					non_deterministic++;
				}
			}
			
			String name = e.getAttribute("id");
			String code = SPLrepository.getAsset(name);
			String[] lines = code.split("\n");
			
			Stack<Integer> stack = new Stack<Integer>();
			for(int i=0; i<lines.length; i++) {
				String line = lines[i];
				if(line.trim().equals("")) continue;
				if (line.startsWith("//#if")) {
					int n = line.split("\\|").length;
					if(n > products.size()) {
						System.out.println("ERROR");
					} else if(n == products.size()) {
						stack.add(3);
					} else if(n > 1) {
						stack.add(2);
						VP++;
						CV++;
					} else if(n == 1) {
						stack.add(1);
						VP++;
						CV++;
					} else {
						System.out.println("ERROR");
					}
				} else if(line.startsWith("//#elif")) {
					stack.pop();
					int n = line.split("\\|").length;
					if(n > products.size()) {
						System.out.println("ERROR");
					} else if(n == products.size()) {
						stack.add(3);
						System.out.println("Wired");
					} else if(n > 1) {
						stack.add(2);
						CV++;
					} else if(n == 1) {
						stack.add(1);
						CV++;
					} else {
						System.out.println("ERROR");
					}
				} else if(line.startsWith("//#endif")) {
					stack.pop();
				} else {
					if(stack.isEmpty()) continue;
					if(stack.peek() == 3) CL++;
					else if(stack.peek() == 2) SL++;
					else if(stack.peek() == 1) UL++;
				}
			}
		}
		System.out.println("@ VP (Variation Points)");
		System.out.println("@ CV (Code Variants of variation points)");
		System.out.println("@ CL (Common Lines)");
		System.out.println("@ SL (Shared Lines that is in V related with multi-products)");
		System.out.println("@ UL (Unique Lines that is in V related with only one product)");
		System.out.println(String.format("=> Migrated Products:%d", products.size()));
		System.out.println(String.format("   Assets:%d (Common:%d , Shared:%d , Unique:%d)", total, common, shared, unique));
		System.out.println(String.format("   ReusedAssets:%d (Deterministic:%d , Non-deterministic:%d)", (common+shared), deterministic, non_deterministic));
		System.out.println(String.format("   PlatformAssets:%d", platform));
		System.out.println(String.format("   VP:%d, CV:%d, CL:%d, SL:%d, UL:%d", VP, CV, CL, SL, UL));
		System.out.println();
	}

	public static void analyzeAntennaSplProject(String dir) {
		int VP = 0;
		int V = 0;
		int CL = 0;
		int VL = 0;
		for(String path : FileExplorer.collectJavaFiles(dir)) {
			String code = MyFileReader.read(path);
			String[] lines = code.split("\n");
			Stack<Boolean> stack = new Stack<Boolean>();
			stack.add(true);
			for(String line : lines) {
				if(line.contains("#if")) {
					stack.add(false);
					VP++;
					V++;
				} else if(line.contains("#elif")) {
					stack.pop();
					stack.add(false);
					V++;
				} else if(line.contains("#endif")){
					stack.pop();
				} else {
					String tl = line.trim();
					if(tl.isEmpty()) continue;
					if(tl.startsWith("//") && !tl.startsWith("//@") && !tl.startsWith("//#")) {
						System.out.println(tl);
						continue;
					}
					if(stack.peek() == true) {
						CL++;
					} else {
						VL++;
					}
				}
			}
		}		
		String str = String.format("VP:%d, V:%d, CL:%d, VL:%d", VP, V, CL, VL); 
		System.out.println(str);
	}

	public static void analyzeJavappSplProject(String dir) {
		String[] exceptions = {
			dir+"\\argouml-core-tools",
			dir+"\\argouml-core-diagrams-sequence2\\tests",
			dir+"\\argouml-core-model\\tests",
			dir+"\\argouml-core-model-mdr\\tests",
			dir+"\\ArgoUMLSPLBenchmark",
		};
		int VP = 0;
		int V = 0;
		int CL = 0;
		int VL = 0;
		int all = 0;
		int common = 0;
		for(String path : FileExplorer.collectJavaFiles(dir)) {
			if(isUnused(exceptions, path)) continue;
			all++;
			File file = new File(path);
			CompilationUnit cu = null;
			try {
				cu = StaticJavaParser.parse(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TokenRange range = cu.getTokenRange().get();
			StringBuffer buffer = new StringBuffer();
			for(JavaToken token : range) {
				switch(token.getKind()) {
				case 5: buffer.append("\n"+token.getText()); break;
				case 8: buffer.append(" ");  break;
				case 9: buffer.append(" ");  break;
				default: 
					buffer.append(token.getText());
				}
			}
			String text = buffer.toString();
			String[] lines = text.split("\n");
			Stack<Boolean> stack = new Stack<Boolean>();
			stack.add(true);
			int cl = 0;
			for(String line : lines) {
				String tl = line.trim();
				if(tl.isEmpty()) continue;
				if(tl.startsWith("//#if")) {
					VP++;
					V++;
					stack.add(false);
				} else if(tl.startsWith("//#elif")) {
					stack.pop();
					V++;
					stack.add(false);
				} else if(tl.startsWith("//#else")) {
					stack.pop();
					V++;
					stack.add(false);
				} else if(tl.startsWith("//#endif")) {
					stack.pop();
				} else {
					if(line.startsWith("//")) { continue; }
					if(stack.peek()) cl++;
					else VL++;
				}
			}
			CL += cl;
			if(cl > 0) common++;
		}
		System.out.println("ALL:"+all+", Common:"+common);
		String str = String.format("VP:%d, V:%d, CL:%d, VL:%d", VP, V, CL, VL); 
		System.out.println(str);
	}

	private static boolean isUnused(String[] exceptions, String path) {
		for(String prefix : exceptions) {
			if(path.startsWith(prefix)) return true;
		}
		return false;
	}
	
	public static void checkArgoUML() {
		String[] products = {
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P01_AllDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P02_AllEnabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P03_LoggingDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P04_CognitiveDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P05_SequenceDiagramDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P06_UseCaseDiagramDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P07_DeploymentDiagramDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P08_CollaborationDiagramDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P09_StateDiagramDisabled.config",
			"C:\\Users\\windows\\eclipse-workspace\\ExtractiveSPL\\examples\\ArgoUML-TraditionalVariants\\P10_ActivityDiagramDisabled.config"
		};
		checkBuildPart(products);
		checkPart(products);
	}

	private static void checkPart(String[] products) {
		Set<String> all = new HashSet<String>();
		for(String dir : products) {
			for(String path : FileExplorer.collectJavaFiles(dir)) {
				String p = path.replace(dir, "");
				all.add(p);
			}
		}
		String[] dirs = {
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-app",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-build",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-diagrams-sequence2",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-infra",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-model",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-model-euml",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-model-mdr",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\argouml-core-tools",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\org.splevo.casestudy.argoumlspl.generator",
			"C:\\Users\\windows\\Desktop\\argouml-spl-benchmark-master\\README_images"
		};
		int count = 0;
		Set<String> diff = new HashSet<String>();
		for(String dir : dirs) {
			for(String path : FileExplorer.collectJavaFiles(dir)) {
				if(path.endsWith("tests\\org\\argouml\\model\\InitializeModel.java")) {
					System.out.println(path);
				}
				String p = path.replace(dir, "");
				if(all.contains(p)) {
					count++;
				} else {
					diff.add(p);
//					System.out.println(path);
				}
			}
		}
		System.out.println("Count:"+count+", Diff:"+diff.size());
	}
	
	private static void checkBuildPart(String[] products) {
		String[] targets = {
			"\\src\\javax",
			"\\src\\org\\omg"
		};
		Set<String> set = new HashSet<String>();
		Set<String> common = null;
		for(String dir : products) {
			Set<String> temp = new HashSet<String>();
			for(String target : targets) {
				for(String path : FileExplorer.collectJavaFiles(dir+target)) {
					String p = path.replace(dir, "");
					set.add(p);
					if(common==null || common.contains(p)) {
						temp.add(p);
					}
				}
			}
			common = temp;
		}
		System.out.println("BUILD:"+set.size() + "(common:"+common.size()+")");
	}

}

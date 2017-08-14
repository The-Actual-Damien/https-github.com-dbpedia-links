package org.dbpedia.links;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.log4j.Logger;
import org.dbpedia.links.lib.GenerateLinks;
import org.dbpedia.links.lib.Metadata;
import org.dbpedia.links.lib.RDFUnitValidate;
import org.dbpedia.links.lib.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CLI {
    private static Logger L = Logger.getLogger(CLI.class);


    private static OptionParser getCLIParser() {

        OptionParser parser = new OptionParser();

        parser.accepts("basedir", "Path to the directory under which repositories would be searched; defaults to '.'")
                .withRequiredArg().ofType(String.class)
                .defaultsTo(".");
        parser.accepts("outdir", "Path to the directory where results are written; defaults to 'current'")
                .withRequiredArg().ofType(String.class)
                .defaultsTo("current");
        parser.accepts("errorlog", "Path to error log (JSON file); defaults to " + System.getProperty("user.dir") + File.separator + "logs" + File.separator + "validaterepo_errorlog.json")
                .withRequiredArg().ofType(File.class)
                .defaultsTo(new File(getDefaultErrorLogDir(), "validaterepo_errorlog.json"));
        parser.accepts("validate", "enables extensive validation, i.e. with SHACL/RDFUNIT and also validation of links");
        parser.accepts("generate", "enables the generation of links, if the option is not set, the tool will just parse all the metadata files in memory");
        parser.accepts("help", "prints help information");

        return parser;
    }

    private static File getDefaultErrorLogDir() {
        File defaultLogsDir = new File(System.getProperty("user.dir") + File.separator + "logs");
        defaultLogsDir.mkdirs();

        return defaultLogsDir;
    }

    public static void main(String[] args) throws IOException{

        OptionParser parser = getCLIParser();
        OptionSet options = null;

        try {
            options = parser.parse(args);

        } catch (OptionException oe) {
            parser.printHelpOn(System.err);
            System.out.println("Error:\t" + oe.getMessage());
            System.exit(1);
        }

        if (options.hasArgument("help")) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        final boolean validate;
        if(options.has("validate")){
            validate = true;
        }else {
            validate =false;
        }

        final boolean generate;
        if(options.has("generate")){
            generate = true;
        }else {
            generate =false;
        }


        String basedir = (String) options.valueOf("basedir");
        String outdir = (String) options.valueOf("outdir");
        File f = new File(Paths.get(basedir).toAbsolutePath().normalize().toString()).getAbsoluteFile();
        List<File> allFilesInRepo = Utils.getAllMetadataFiles(f);
        RDFUnitValidate rval = new RDFUnitValidate();
        GenerateLinks gl = new GenerateLinks();

        allFilesInRepo.stream().forEach(one->{
            try {
                Metadata m = Metadata.create(one, validate, rval);

                if(generate) {
                    gl.generateLinkSets(m,outdir);
                }
            }catch (Exception e){
               //e.printStackTrace();
               L.error(e.toString());
            }
        });


        /*checkRdfSyntax(Utils.filterFileWithEndsWith(allFilesInRepo, ".nt"));
        checkRdfSyntax(Utils.filterFileWithEndsWith(allFilesInRepo, ".ttl"));
        checkRdfSyntax(Utils.filterFileWithEndsWith(allFilesInRepo, ".n3"));
//

        checkRdfSyntaxForCompressed(Utils.filterFileWithEndsWith(allFilesInRepo, ".nt.bz2"));


        checkDBpediaAsSubject(Utils.filterFileWithEndsWith(allFilesInRepo, "links.nt"));
        checkDBpediaAsSubject(Utils.filterFileWithEndsWith(allFilesInRepo, "links.ttl"));
        //
        checkDBpediaAsSubjectForCompressed(Utils.filterFileWithEndsWith(allFilesInRepo, "links.nt.bz2"));


        checkFolderStructure(allFilesInRepo);
//
        checkMetadataFilesWithRdfUnit(Utils.filterFileWithEndsWith(allFilesInRepo, "metadata.ttl"));
//
        checkMetadataLinks(Utils.filterFileWithEndsWith(allFilesInRepo, "metadata.ttl"));
*/

    }


}
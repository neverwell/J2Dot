package com.neverwell.dot;

import com.intellij.openapi.editor.Document;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizException;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.parse.ParserException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author neverwell
 * @date 2021/11/6
 * @Description:
 */
public class DotUtils {
    public static void main(String[] args) throws IOException {
        DotUtils.class.getResource("/dot/ast.dot");

        //try (InputStream dot = getClass().getResourceAsStream("/color.dot")) {
        //    MutableGraph g = new Parser().read(dot);
        //    Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File("example/ex4-1.png"));
        //
        //    g.graphAttrs()
        //            .add(Color.WHITE.gradient(Color.rgb("888888")).background().angle(90))
        //            .nodeAttrs().add(Color.WHITE.fill())
        //            .nodes().forEach(node ->
        //            node.add(
        //                    Color.named(node.name().toString()),
        //                    Style.lineWidth(4), Style.FILLED));
        //    Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File("example/ex4-2.png"));
        //}
        //Graph g = graph("example1").directed()
        //        .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
        //        .nodeAttr().with(Font.name("arial"))
        //        .linkAttr().with("class", "link-class")
        //        .with(
        //                node("a").with(Color.RED).link(node("b")),
        //                node("b").link(
        //                        to(node("c")).with(attr("weight", 5), Style.DASHED)
        //                )
        //        );
        //Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("example/ex1.png"));
        BufferedImage bufferedImage;
        Document image;

        try (InputStream dot = DotUtils.class.getResourceAsStream("/dot/ast.dot")) {
            //MutableGraph g = new Parser().read(dot);
            Graphviz graphviz = Graphviz.fromGraph(new Parser().read(dot));
            graphviz.width(700).height(700).render(Format.PNG).toImage();
        } catch (IOException | ParserException | GraphvizException e) {
        }
    }
}

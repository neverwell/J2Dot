## ${topic}
[TOC]
<#list noteList as note>
### ${note.title}
- ${note.mark}
- ${note.fileName}
```${note.fileType}
${note.content}
```
<#if image??>
    ```dot
    ${note.img}
    ```
<#else>
</#if>
</#list>





<#if image??>
    <script src="https://unpkg.com/d3@5.16.0/dist/d3.min.js"></script>
    <script src="https://unpkg.com/@hpcc-js/wasm@0.3.11/dist/index.min.js"></script>
    <script src="https://unpkg.com/d3-graphviz@3.1.0/build/d3-graphviz.min.js"></script>
    <script>
        function d3ize(elem) {
            var par = elem.parentElement;
            d3.select(par).append('div').graphviz().renderDot(elem.innerText);
            <!--d3.select(par).append('div').attr('class', 'graphviz-svg').graphviz().width(
              par.clientWidth - 100 * parseFloat(getComputedStyle(par).fontSize)
            ).renderDot(elem.innerText);
            -->
            d3.select(elem).style('display', 'none');

        }
        console.log(document.getElementsByClassName(".language-dot"));
        var dotelems = document.getElementsByClassName("language-dot");
        for (let elem of dotelems) {
            d3ize(elem);
        }
    </script>
<#else>
</#if>
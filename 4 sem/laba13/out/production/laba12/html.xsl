<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:variable name="apos">'</xsl:variable>
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="windows-1251"></meta>
                <title>Конфетки</title>
                <link rel="shortcut icon" href="https://rating.chgk.info/favicon.yellow.ico"></link>
                <style>
                    body {
                    background: #f4f0cb;
                    }
                    table {
                    color: #271d0c;
                    width: 95%;
                    font-size: 16px;
                    margin: 20px;
                    border: #271d0c 1px solid;
                    border-radius: 3px;
                    box-shadow: 0 1px 2px #5f461b;
                    }

                    th {
                    padding: 21px 25px 22px 25px;
                    background: #cc99ff;
                    text-align: center
                    }

                    tr {
                    text-align: center;
                    padding-left: 20px;
                    }

                    td {
                    padding: 18px;
                    border-left: 1px solid #cc99ff;
                    border-right: 1px solid #cc99ff;
                    }

                    tr, th {
                    overflow-wrap: break-word;
                    word-wrap: break-word;
                    }

                    tr:nth-child(even) {
                    background-color: #cc99ff
                    }

                    tr:nth-child(odd) {
                    background-color: #ffccff
                    }
                </style>
            </head>
            <body>
                <table>
                    <thead>
                        <tr>
                            <th scope="col">Индекс</th>
                            <th scope="col">Название</th>
                            <th scope="col">Фабрика</th>
                            <th scope="col">Вес</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="sweets/sweet">
                            <tr>
                                <td>
                                    <xsl:value-of select="@index"/>
                                </td>
                                <td>
                                    <xsl:value-of select="@name"/>
                                </td>

                                <td>
                                    <xsl:value-of select="factory"/>
                                </td>
                                <td>
                                    <xsl:value-of select="weight"/>
                                </td>
                            </tr>
                        </xsl:for-each>

                        <tr>
                            <td colspan="3">
                                <strong>Всего конфет:</strong>
                            </td>
                            <td>
                                <xsl:value-of select="count(sweets/sweet)"/>
                            </td>
                        </tr>
                        <tr>

                            <td colspan="3">
                                <strong>Средний вес:</strong>
                            </td>
                            <td>
                                <xsl:value-of select="sum(sweets/sweet/weight) div count(sweets/sweet)"/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
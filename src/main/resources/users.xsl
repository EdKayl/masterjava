<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!--<xsl:output method="text" omit-xml-declaration="yes" indent="no"/>-->
    <!--<xsl:strip-space elements="*"/>-->
    <xsl:template match="/">
        <html>
            <body>
                <table>
                    <tr>
                        <th>User name</th>
                        <th>email address</th>
                    </tr>
                    <xsl:for-each select="/*[name()='Payload']/*[name()='Users']/*[name()='User']">
                        <tr>
                            <th><xsl:value-of select="/"/></th>
                            <th><xsl:value-of select="@email"/></th>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>